package it.unipr.scarpenti.ant.ui;

import java.awt.Font;
import java.awt.Window;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;

import it.unipr.scarpenti.ant.AppData;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class AntConfigPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 538457619650726340L;
	private JTextField txtOutputFolder;
	private JTextField txtModelPath;
	private AppData appData;
	private JRadioButton rdbtnM1;
	private JRadioButton rdbtnM2;
	private JRadioButton rdbtnYesArff;
	private JRadioButton rdbtnNoArff;
	private JRadioButton rdbtnYouPlay;
	private JRadioButton rdbtnIaPlay;
	private JTextField txtSeed;
	private JCheckBox chckbxEnableSeed;
	private JRadioButton rdbtnLoopOnSeedsNo;
	private JRadioButton rdbtnLoopOnSeedsYes;
	private JTextField txtLoopCount;
	private JLabel lblAutoPlay;
	private JRadioButton rdbtnAutoplayNo;
	private JRadioButton rdbtnAutoplayYes;
	private JLabel lblCount;

	/**
	 * Create the panel.
	 */
	public AntConfigPanel(AppData appData) {
		this.appData = appData;
		setLayout(new MigLayout("", "[][][][][right][grow,left][36.00,left][][][grow][][][]", "[][][][][][][][][][][]"));

		JLabel lblAntConfiguration = new JLabel("ANT Game");
		lblAntConfiguration.setFont(new Font("Tahoma", Font.BOLD, 15));
		add(lblAntConfiguration, "cell 0 0 13 1,alignx center");

		JLabel lblChiGioca = new JLabel("Who play?  ");
		add(lblChiGioca, "cell 4 1");

		rdbtnYouPlay = new JRadioButton("You");
		add(rdbtnYouPlay, "cell 5 1");

		rdbtnIaPlay = new JRadioButton("IA");
		add(rdbtnIaPlay, "cell 6 1");

		JLabel lblVisibilitm = new JLabel("Visibility (m) : ");
		add(lblVisibilitm, "cell 4 2");

		rdbtnM1 = new JRadioButton("1");
		add(rdbtnM1, "cell 5 2");

		rdbtnM2 = new JRadioButton("2");
		add(rdbtnM2, "cell 6 2");

		JLabel lblScriviArffFile = new JLabel("Write arff file : ");
		add(lblScriviArffFile, "cell 4 3");

		rdbtnYesArff = new JRadioButton("Yes");
		add(rdbtnYesArff, "cell 5 3");

		rdbtnNoArff = new JRadioButton("No");
		add(rdbtnNoArff, "cell 6 3");

		JLabel lblCartellaDiOutput = new JLabel("Output folder : ");
		add(lblCartellaDiOutput, "cell 4 4,alignx trailing");

		txtOutputFolder = new JTextField();
		add(txtOutputFolder, "cell 5 4 7 1,growx");
		txtOutputFolder.setColumns(10);

		JButton btnSelectoutputFolder = new JButton("Select a folder ...");
		add(btnSelectoutputFolder, "cell 12 4");
		btnSelectoutputFolder.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setCurrentDirectory(new File(txtOutputFolder.getText()));
			int retValue = fileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));
			if (retValue == JFileChooser.APPROVE_OPTION) {
				txtOutputFolder.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});

		JLabel lblModelPath = new JLabel("model path (ony for IA) : ");
		add(lblModelPath, "cell 4 5,alignx trailing");

		txtModelPath = new JTextField();
		txtModelPath.setColumns(10);
		add(txtModelPath, "cell 5 5 7 1,growx");

		JButton btnSelectModelFile = new JButton("Select a file ...");
		add(btnSelectModelFile, "cell 12 5");
		btnSelectModelFile.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setCurrentDirectory(new File(txtModelPath.getText()));
			int retValue = fileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));
			if (retValue == JFileChooser.APPROVE_OPTION) {
				txtModelPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});

		JButton btnNewButton = new JButton("Save and Play");
		btnNewButton.addActionListener(this);
		
		lblAutoPlay = new JLabel("auto play:");
		add(lblAutoPlay, "cell 4 6,alignx trailing");
		
		rdbtnAutoplayNo = new JRadioButton("No");
		add(rdbtnAutoplayNo, "cell 5 6");
		
		rdbtnAutoplayYes = new JRadioButton("Yes");
		add(rdbtnAutoplayYes, "cell 6 6");
		
		JLabel lblLoopOnSeeds = new JLabel("Loop on seeds:");
		add(lblLoopOnSeeds, "cell 4 7");
		
		rdbtnLoopOnSeedsNo = new JRadioButton("No");
		rdbtnLoopOnSeedsNo.setSelected(true);
		add(rdbtnLoopOnSeedsNo, "cell 5 7");
		
		rdbtnLoopOnSeedsYes = new JRadioButton("Yes");
		add(rdbtnLoopOnSeedsYes, "cell 6 7");
		
		chckbxEnableSeed = new JCheckBox("");
		chckbxEnableSeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				txtSeed.setEnabled(chckbxEnableSeed.isSelected());
				if (!chckbxEnableSeed.isSelected())
					txtSeed.setText("");
			}
		});
		
		lblCount = new JLabel("count");
		add(lblCount, "cell 8 7,alignx trailing");
		
		txtLoopCount = new JTextField();
		add(txtLoopCount, "cell 9 7 3 1,growx");
		txtLoopCount.setColumns(10);
		add(chckbxEnableSeed, "flowx,cell 4 8");
		
		JLabel lblSeedNumber = new JLabel("seed number : ");
		add(lblSeedNumber, "cell 4 8,alignx trailing");
		
		txtSeed = new JTextField();
		txtSeed.setEnabled(false);
		add(txtSeed, "cell 5 8 7 1,growx");
		txtSeed.setColumns(10);
		add(btnNewButton, "cell 5 10 3 1,alignx center,aligny bottom");

		ButtonGroup groupM = new ButtonGroup();
		groupM.add(rdbtnM1);
		groupM.add(rdbtnM2);

		ButtonGroup groupWhoPlay = new ButtonGroup();
		groupWhoPlay.add(rdbtnIaPlay);
		groupWhoPlay.add(rdbtnYouPlay);

		ButtonGroup groupWriteArff = new ButtonGroup();
		groupWriteArff.add(rdbtnNoArff);
		groupWriteArff.add(rdbtnYesArff);

		ButtonGroup groupLoopOnSeeds = new ButtonGroup();
		groupLoopOnSeeds.add(rdbtnLoopOnSeedsNo);
		groupLoopOnSeeds.add(rdbtnLoopOnSeedsYes);
		
		
		rdbtnLoopOnSeedsYes.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				txtLoopCount.setEnabled(rdbtnLoopOnSeedsYes.isSelected());
			}
		});
		
		ButtonGroup groupAutoplay = new ButtonGroup();
		groupAutoplay.add(rdbtnAutoplayNo);
		groupAutoplay.add(rdbtnAutoplayYes);
		
		// init Fields
		initFields();

	}

	private void initFields() {
		int visualField = appData.getVisualField();
		String whoPlay = appData.getWhoPlay();
		boolean writeArff = appData.isWriteArffOn();
		rdbtnM1.setSelected(visualField == 1);
		rdbtnM2.setSelected(visualField == 2);
		rdbtnIaPlay.setSelected(AppData.PLAYER_IA.equals(whoPlay));
		rdbtnYouPlay.setSelected(!AppData.PLAYER_IA.equals(whoPlay));
		rdbtnYesArff.setSelected(writeArff);
		rdbtnNoArff.setSelected(!writeArff);
		txtOutputFolder.setText(appData.getOutputFolder());
		txtModelPath.setText(appData.getModelPath());
		if (appData.getSeedNumber() != null)
			txtSeed.setText(String.valueOf(appData.getSeedNumber()));
		chckbxEnableSeed.setSelected(appData.getSeedNumber() != null);
		rdbtnLoopOnSeedsYes.setSelected(appData.isLoopOnSeedsOn());
		rdbtnLoopOnSeedsNo.setSelected(!appData.isLoopOnSeedsOn());
		txtLoopCount.setEnabled(rdbtnLoopOnSeedsYes.isSelected());
		if (appData.getLoopCount() != null)
			txtLoopCount.setText(String.valueOf(appData.getLoopCount()));
		rdbtnAutoplayNo.setSelected(!appData.isAutoPlayOn());
		rdbtnAutoplayYes.setSelected(appData.isAutoPlayOn());
		

	}

	private String addSlash(String folderPath) {
		if (StringUtils.isNotBlank(folderPath) && !folderPath.endsWith("/") && !folderPath.endsWith("\\"))
			return folderPath+"/";
		else
			return folderPath;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			appData.setVisualField(rdbtnM1.isSelected() ? 1 : 2);
			appData.setWhoPlay(rdbtnIaPlay.isSelected() ? AppData.PLAYER_IA : AppData.PLAYER_YOU);
			appData.setWriteArffOn(rdbtnYesArff.isSelected());
			appData.setOutputFolder(addSlash(txtOutputFolder.getText()));
			appData.setModelPath(txtModelPath.getText());
			if (chckbxEnableSeed.isSelected())
				appData.setSeedNumber(Integer.parseInt(txtSeed.getText()));
			else
				appData.setSeedNumber(null);
			appData.setLoopOnSeeds(rdbtnLoopOnSeedsYes.isSelected());
			
			if (rdbtnLoopOnSeedsYes.isSelected())
				appData.setLoopCount(Integer.parseInt(txtLoopCount.getText()));
			else
				appData.setLoopCount(null);
			
			appData.setAutoPlayOn(rdbtnAutoplayYes.isSelected());
			
			appData.saveProperties();
			
		} catch (Exception e1) {
			String message = e1.getMessage();
			if (e1 instanceof NumberFormatException)
				message = "seed: valore non numerico";
			e1.printStackTrace();
			System.out.println("END");
			JOptionPane.showMessageDialog(this, message, "ERRORE", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		// close
		Window window = SwingUtilities.getWindowAncestor(this);
		window.dispose();
	}

}
