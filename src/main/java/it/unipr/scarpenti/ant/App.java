package it.unipr.scarpenti.ant;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;

import it.unipr.scarpenti.ant.ui.AntConfigPanel;
import it.unipr.scarpenti.ant.ui.GamePlay;

/**
 * Hello world!
 *
 */
public class App {

	public static final int PANEL_SIZE = 600;

	public static void main(String[] args) throws Exception {

		final AppData appData = new AppData();
		
		JFrame mainFrame = new JFrame();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE + 20));
		mainFrame.pack();
		mainFrame.setLocation(dim.width / 2 - mainFrame.getSize().width / 2, dim.height / 2 - mainFrame.getSize().height / 2);
		mainFrame.setResizable(false);
		mainFrame.setTitle("ANT");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//modal di configurazione iniziale
		JDialog configFrame2 = new JDialog(mainFrame, "ANT - config", true);
		configFrame2.getContentPane().add(new AntConfigPanel(appData));
		//configFrame2.setPreferredSize(new Dimension(500, 500));
		configFrame2.pack();
		configFrame2.setLocation(dim.width / 2 - configFrame2.getSize().width / 2, dim.height / 2 - configFrame2.getSize().height / 2);
		configFrame2.setResizable(false);
		configFrame2.setVisible(true);
		
		
		mainFrame.getContentPane().add(new GamePlay(PANEL_SIZE, appData));
		mainFrame.setVisible(true);

	}

}
