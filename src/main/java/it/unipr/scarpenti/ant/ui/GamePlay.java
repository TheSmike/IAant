package it.unipr.scarpenti.ant.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import it.unipr.scarpenti.ant.Ant;
import it.unipr.scarpenti.ant.AppData;
import it.unipr.scarpenti.ant.ArffFile;
import it.unipr.scarpenti.ant.Chessboard;
import it.unipr.scarpenti.ant.Direction;
import it.unipr.scarpenti.ant.Position;
import it.unipr.scarpenti.ant.exception.AntGameException;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class GamePlay extends JPanel implements KeyListener {

	/* EDITABLE CONST */
	private static final int N = 10;
	private final int visualField;
	private final int PANEL_SIZE;
	private final int SQUARE_DIM;

	/* STATIC CONST */
	private static final long serialVersionUID = 6353883395159239509L;
	private static final String IMG_PATH = "src/main/resources/img/";
	private static final ImageIcon ANT_RIGHT = getImage("antRightSmall.png");
	private static final ImageIcon FOOD = getImage("watermelon.png");
	private static final int FOOD_X_OFFSET = 5;
	private static final int FOOD_Y_OFFSET = 15;
	private static final int SCORE_X_OFFSET = 18;
	private static final int SCORE_Y_OFFSET = 15;
	private static final int SQUARE_X_OFFSET = 0;
	private static final int SQUARE_Y_OFFSET = 0;
	private static final int START_POS_X = - 11;  
	private static final int START_POS_Y = + 8; 

	private int xPos;
	private int yPos;
	private int score;
	private boolean endRound = false;
	private Chessboard chessboard;
	private Ant ant;
	private ArffFile arffFile;
	private int moves;
	private Throwable throwedException = null;
	private boolean newRound;
	private int matchNumber;
	private AppData appData;
	private J48 classifier;

	public GamePlay(int panelSize, AppData appData) throws Exception {
		super();
		visualField = appData.getVisualField();
		PANEL_SIZE = panelSize;
		this.appData = appData;
		SQUARE_DIM = 50;

		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		arffFile = new ArffFile(visualField, this.appData);
		newRound = true;
		matchNumber = 0;
		
		if (AppData.PLAYER_IA.equals(appData.getWhoPlay())) {
			classifier = (J48) SerializationHelper.read(appData.getModelPath());
			System.out.println("classifier J48: " + classifier);
		}
	}

	private void initRound() {
		score = 0;
		moves = 2 * N;
		chessboard = new Chessboard(N);
		Position initAntPosition = chessboard.initBaord();
		ant = new Ant(initAntPosition);

		System.out.println(ant);
		xPos = START_POS_X + (ant.getAntPosition().getC()) * SQUARE_DIM;
		yPos = START_POS_Y + (ant.getAntPosition().getR()) * SQUARE_DIM;
		newRound = false;
		endRound = false;
		
		try {
			arffFile.writeComment("% match " + ++matchNumber);
		} catch (AntGameException e) {
			throwedException = e;
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		if (newRound)
			initRound();

		//handleError
		if (throwedException != null) {
			throwedException.printStackTrace();
			System.out.println("END");
			JOptionPane.showMessageDialog(this, throwedException.getMessage(), "ERRORE", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		g.setColor(Color.RED);
		g.fillRect(0, 0, PANEL_SIZE, PANEL_SIZE);
		g.setColor(Color.darkGray);
		g.fillRect(SQUARE_DIM, SQUARE_DIM, PANEL_SIZE - SQUARE_DIM * 2, PANEL_SIZE - SQUARE_DIM * 2);

		// disegna righe griglia
		g.setColor(Color.lightGray);
		for (int i = 0; i < N + 1; i++) {
			g.drawLine(SQUARE_DIM * (1 + i), SQUARE_DIM, SQUARE_DIM * (1 + i), PANEL_SIZE - SQUARE_DIM); // rows
			g.drawLine(SQUARE_DIM, SQUARE_DIM * (1 + i), PANEL_SIZE - SQUARE_DIM, SQUARE_DIM * (1 + i)); // cols
		}
		// disegna sfondo caselle intorno
		for (int r = 0; r < N + 2; r++) {
			for (int c = 0; c < N + 2; c++) {
				Integer value = chessboard.getSquareValue(r, c);
				if (value != null) {
					if (value > -N - 2 && value < 0) {
						g.setColor(new Color(128, 133, 0));
						g.fillRect((c * SQUARE_DIM) + 1 + SQUARE_X_OFFSET, (r * SQUARE_DIM) + 1 + SQUARE_Y_OFFSET,
								SQUARE_DIM - 1, SQUARE_DIM - 1);
					}
					g.setColor(Color.WHITE);
					char[] valueStr = String.valueOf(value).toCharArray();
					g.drawChars(valueStr, 0, valueStr.length, (c) * SQUARE_DIM + SCORE_X_OFFSET,
							r * SQUARE_DIM + SCORE_Y_OFFSET);

				}
			}
		}

		// disegna formica
		ANT_RIGHT.paintIcon(this, g, xPos, yPos);

		// disegna cibo
		List<Position> foodList = chessboard.getFoodList();
		for (Position position : foodList) {
			FOOD.paintIcon(this, g, (position.getC()) * SQUARE_DIM + FOOD_X_OFFSET,
					(position.getR()) * SQUARE_DIM + FOOD_Y_OFFSET);
		}

		// oscura zone non visibili
		if (!endRound) {
			g.setColor(Color.BLACK);
			myFillRect(g, 0, -8, PANEL_SIZE, yPos - SQUARE_DIM * visualField); // UP square
			myFillRect(g, 0, 0, xPos - SQUARE_DIM * visualField + 11, PANEL_SIZE);
			myFillRect(g, xPos + 62 + SQUARE_DIM * visualField, 0, PANEL_SIZE - (xPos + 53 + SQUARE_DIM * visualField),
					PANEL_SIZE);
			myFillRect(g, 0, yPos + 43 + SQUARE_DIM * visualField, PANEL_SIZE,
					PANEL_SIZE - (yPos + 36 + SQUARE_DIM * visualField));
		}
		// CHECKME ci vuole?
		g.dispose();

	}

	private void myFillRect(Graphics g, int x, int y, int width, int height) {
		if (width > 0 && height > 0)
			g.fillRect(x, y, width, height);

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (endRound) {
			handleEndRound();
			repaint();
			return;
		}

		boolean posChanged = false;
		if (appData.getWhoPlay().equals(AppData.PLAYER_IA)) {
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER || arg0.getKeyCode() == KeyEvent.VK_SPACE) {
				posChanged = true;

				// Declare two numeric attributes
				Attribute Attribute1 = new Attribute("index-0-0");
				Attribute Attribute2 = new Attribute("index-0-1");
				Attribute Attribute3 = new Attribute("index-0-2");

				Attribute Attribute4 = new Attribute("index-1-0");
				Attribute Attribute5 = new Attribute("index-1-1");
				Attribute Attribute6 = new Attribute("index-1-2");

				Attribute Attribute7 = new Attribute("index-2-0");
				Attribute Attribute8 = new Attribute("index-2-1");
				Attribute Attribute9 = new Attribute("index-2-2");

				// Declare the class attribute along with its values
				FastVector fvClassVal = new FastVector(4);
				fvClassVal.addElement("UP");
				fvClassVal.addElement("DOWN");
				fvClassVal.addElement("RIGTH");
				fvClassVal.addElement("LEFT");
				Attribute ClassAttribute = new Attribute("direction", fvClassVal);

				// Declare the feature vector
				FastVector fvWekaAttributes = new FastVector(10);
				fvWekaAttributes.addElement(Attribute1);
				fvWekaAttributes.addElement(Attribute2);
				fvWekaAttributes.addElement(Attribute3);
				fvWekaAttributes.addElement(Attribute4);
				fvWekaAttributes.addElement(Attribute5);
				fvWekaAttributes.addElement(Attribute6);
				fvWekaAttributes.addElement(Attribute7);
				fvWekaAttributes.addElement(Attribute8);
				fvWekaAttributes.addElement(Attribute9);
				fvWekaAttributes.addElement(ClassAttribute);

				Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);
				isTrainingSet.setClassIndex(9);

				Instance instance = new DenseInstance(10);

				int[][] neighbourhood = chessboard.getChessBoardNeighbourhood(ant.getAntPosition(), visualField);
				for (int r = 0; r < neighbourhood.length; r++) {
					for (int c = 0; c < neighbourhood.length; c++) {
						instance.setValue(r * neighbourhood.length + c, neighbourhood[r][c]);
						// instance.setValue(r*neighbourhood.length + c, neighbourhood[r][c]);
					}
				}
				try {
					// Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);
					instance.setDataset(isTrainingSet);
					System.out.println("instance => "  + instance);
					double[] fDistribution = classifier.distributionForInstance(instance);
					
					double max = 0;
					int index = -1;
					for (int i = 0; i < fDistribution.length; i++) {
						System.out.println("fDistribution[i]=" + fDistribution[i]);
						if (fDistribution[i] > max) {
							index = i;
							max = fDistribution[i];
						}
					}
					System.out.println("direction : " + index + ", likelihood : " + max);
					switch (index) {
					case 0:
						arg0.setKeyCode(KeyEvent.VK_UP);
						break;
					case 1:
						arg0.setKeyCode(KeyEvent.VK_DOWN);
						break;
					case 2:
						arg0.setKeyCode(KeyEvent.VK_LEFT);
						break;
					case 3:
						arg0.setKeyCode(KeyEvent.VK_RIGHT);
						break;
					default:
						break;
					}

					// classifier.classifyInstance(instance);
				} catch (Exception e) {
					throwedException = e;
				}
			} else
				return;
		}

			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_UP:
				yPos -= SQUARE_DIM;
				ant.moveUp();
				posChanged = true;
				break;
			case KeyEvent.VK_DOWN:
				yPos += SQUARE_DIM;
				ant.moveDown();
				posChanged = true;
				break;
			case KeyEvent.VK_RIGHT:
				xPos += SQUARE_DIM;
				ant.moveRight();
				posChanged = true;
				break;
			case KeyEvent.VK_LEFT:
				xPos -= SQUARE_DIM;
				ant.moveLeft();
				posChanged = true;
				break;

			default:
				break;
			}
		

		if (posChanged) {
			Direction keyCode = Direction.getDirectionFromCode(arg0.getKeyCode());
			moves--;
			System.out.println(ant + ". Mosse rimaste: " + moves);
			int[][] neighbourhood = chessboard.getChessBoardNeighbourhood(ant.getAntPosition(), visualField);
			System.out.println(neighbourhood);
			try {
				arffFile.writeCase(neighbourhood, keyCode);
			} catch (AntGameException e) {
				throwedException = e;
			}

			score += chessboard.getPositionScoreAndDecrease(ant.getAntPosition());
			if (chessboard.isEndOfTheBoard(ant.getAntPosition()) || moves == 0)
				endRound = true;
			// chessboard.logChessboard(ant.getAntPosition());
			repaint();
		}

	}

	private void handleEndRound() {
		System.out.println("END Round");
		int response = JOptionPane.showConfirmDialog(this, "Partita n° " + 
		matchNumber + "\nPunteggio : " + score + "\nVuoi fare un'altra partita?\n(Arricchirai il data set)", "Fine round", JOptionPane.YES_NO_OPTION);
		switch (response) {
		case JOptionPane.YES_OPTION:
			newRound = true;
			break;
		default:
			System.exit(0);
			break;
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// titleImage = new ImageIcon(getImage("ant_small.png"));
	}

	private static ImageIcon getImage(String imgName) {
		return new ImageIcon(IMG_PATH + imgName);
	}
	
	
	

}
