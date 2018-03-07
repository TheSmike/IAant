package it.unipr.scarpenti.ant;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePlay extends JPanel implements KeyListener {

	/*EDITABLE CONST*/
	private static final int N = 10;
	private final int  M ;
	private final int  VISUAL_FIELD;
	private final int PANEL_SIZE;
	private final int SQUARE_DIM;
	
	/*STATIC CONST*/
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
		
	/*OTHERS*/
	public enum Direction {
		UP,DOWN,RIGHT,LEFT;
	}
	
	private int xPos;
	private int yPos;
	private int score = 0;
	private boolean endGame = false;
	private Chessboard chessboard;
	private Ant ant;
	private ArffFile arffFile; 
	private int moves = 2*N;
	
	
		
	public GamePlay(int panelSize, int m) throws Exception {
		super();
		M = m;
		VISUAL_FIELD = 2 * M + 1;
		PANEL_SIZE = panelSize;
		SQUARE_DIM = 50;
		xPos = 0 - 11; //START_POS_X
		yPos = 0 + 8; //START_POS_Y 
		
		
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);  
		chessboard = new Chessboard(N);
		Position initAntPosition = chessboard.initBaord();
		ant = new Ant(initAntPosition);
		arffFile = new ArffFile(m);
		
		System.out.println(ant);
		xPos += (ant.getAntPosition().getC()) *  SQUARE_DIM;
		yPos += (ant.getAntPosition().getR()) *  SQUARE_DIM;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.RED);
		g.fillRect(0, 0, PANEL_SIZE, PANEL_SIZE);        
		g.setColor(Color.darkGray);
		g.fillRect(SQUARE_DIM, SQUARE_DIM, PANEL_SIZE-SQUARE_DIM*2, PANEL_SIZE-SQUARE_DIM*2);        

		//disegna righe griglia
		g.setColor(Color.lightGray);
		for (int i = 0; i < N+1; i++) {
			g.drawLine(SQUARE_DIM*(1+i), SQUARE_DIM, SQUARE_DIM*(1+i), PANEL_SIZE - SQUARE_DIM); //rows
			g.drawLine(SQUARE_DIM, SQUARE_DIM*(1+i), PANEL_SIZE - SQUARE_DIM, SQUARE_DIM*(1+i)); //cols			
		}
		//disegna sfondo caselle intorno
	    int rAnt = ant.getAntPosition().getR();
	    int cAnt =  ant.getAntPosition().getC();
	    for (int r = 0; r < N+2; r++) {
			for (int c = 0; c < N+2; c++) {	
				Integer value = chessboard.getSquareValue(r,c);
				if (value != null) {
					if (value > -N-2 && value < 0) {
						g.setColor(new Color(128, 133, 0));
						g.fillRect( (c * SQUARE_DIM)+1 + SQUARE_X_OFFSET , (r*SQUARE_DIM)+1 + SQUARE_Y_OFFSET, SQUARE_DIM-1, SQUARE_DIM-1);
					}
					g.setColor(Color.WHITE);
					char[] valueStr = String.valueOf(value).toCharArray();
					g.drawChars(valueStr, 0, valueStr.length, (c) * SQUARE_DIM + SCORE_X_OFFSET , r * SQUARE_DIM + SCORE_Y_OFFSET);
					
				}
			}
	    }	    

		//disegna formica
		ANT_RIGHT.paintIcon(this, g, xPos , yPos);
	    
		//disegna cibo
		List<Position> foodList = chessboard.getFoodList();
		for (Position position : foodList) {
			FOOD.paintIcon(this, g, (position.getC()) * SQUARE_DIM + FOOD_X_OFFSET, (position.getR()) *  SQUARE_DIM + FOOD_Y_OFFSET);			
		}
	    
		//oscura zone non visibili
		if (!endGame) {
			g.setColor(Color.BLACK);
			myFillRect(g, 0, -8, PANEL_SIZE, yPos - SQUARE_DIM*VISUAL_FIELD);  //UP square 
			myFillRect(g, 0, 0, xPos - SQUARE_DIM*VISUAL_FIELD +11, PANEL_SIZE);
			myFillRect(g, xPos+62 + SQUARE_DIM*VISUAL_FIELD, 0, PANEL_SIZE - (xPos+53 + SQUARE_DIM*VISUAL_FIELD), PANEL_SIZE);  
			myFillRect(g, 0, yPos + 43 + SQUARE_DIM*VISUAL_FIELD, PANEL_SIZE, PANEL_SIZE - (yPos + 36 + SQUARE_DIM*VISUAL_FIELD));
		}
		//CHECKME ci vuole?
		g.dispose();
		
	}
	
	private void myFillRect(Graphics g, int x, int y, int width, int height) {
		if (width>0 && height > 0)
			g.fillRect(x, y, width, height);
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (endGame) {
			System.out.println("END");
			JOptionPane.showMessageDialog(this, "Your score : " + score);
			System.exit(0);
		}
		
		boolean posChanged = false;
		String keyCode = "";
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_UP:
			yPos -= SQUARE_DIM;
			ant.moveUp();
			keyCode = "up";
			posChanged = true;
			break;
		case KeyEvent.VK_DOWN:
			yPos += SQUARE_DIM;
			ant.moveDown();
			keyCode = "down";
			posChanged = true;
			break;
		case KeyEvent.VK_RIGHT:
			xPos += SQUARE_DIM;
			ant.moveRight();
			keyCode = "right";
			posChanged = true;
			break;
		case KeyEvent.VK_LEFT:
			xPos -= SQUARE_DIM;
			ant.moveLeft();
			keyCode = "left";
			posChanged = true;
			break;

		default:
			break;
		}  
		
		if (posChanged) {
			moves--;
			System.out.println(ant + ". Mosse rimaste: " + moves);
			List<Integer> neighbourhood = chessboard.getChessBoardNeighbourhood(ant.getAntPosition(), VISUAL_FIELD);
			arffFile.writeCase(neighbourhood, keyCode);
			System.out.println(neighbourhood);
			
			
			score += chessboard.getPositionScoreAndDecrease(ant.getAntPosition());
			if ( chessboard.isEndOfTheBoard(ant.getAntPosition()) || moves == 0)
				endGame = true;
			//chessboard.logChessboard(ant.getAntPosition());
			repaint();
		}
		
	}

	

	private void computeScore() {
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		//titleImage = new ImageIcon(getImage("ant_small.png"));
	}
	
	private static ImageIcon getImage(String imgName) {
		return new ImageIcon(IMG_PATH + imgName);
	}


}
