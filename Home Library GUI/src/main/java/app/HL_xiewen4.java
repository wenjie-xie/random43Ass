package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gui.MainFrame;

/**
 * @author xiewen4
 * This main is use to start the home library application.
 */
public class HL_xiewen4 {
	public static final MainFrame mainFrame = new MainFrame("Home Library");
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				mainFrame.setSize(1500, 800);
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainFrame.setVisible(true);
			}
		});
	}

}
