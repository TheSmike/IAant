package it.unipr.scarpenti.ant;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Hello world!
 *
 */
public class App {

	public static final int PANEL_SIZE = 600;

	public static void main(String[] args) throws Exception {

		JFrame frame = new JFrame();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setPreferredSize(new Dimension(PANEL_SIZE + 16, PANEL_SIZE + 39));
		frame.pack();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setResizable(false);
		frame.setTitle("ANT");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension dimension = frame.getSize();
		System.out.println("dimension = " + dimension);

		System.out.println("App1");
		Container contentPane = frame.getContentPane();
		System.out.println("contentPane size = " + contentPane.getSize());
		int m = Integer.parseInt(PropertiesFactory.getProperties().getProperty("m_for_visibility"));
		contentPane.add(new GamePlay(PANEL_SIZE, m));

		System.out.println("App2");
		frame.setVisible(true);

	}

}
