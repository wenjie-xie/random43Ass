package app;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import gui.pages.DataInsertMoviePanel;

/**
 * This is how you create the JFrame execution
 * @author xiewen4
 *
 */
public class Test {
	
	public static void main(String[] arg) {
		 
	SwingUtilities.invokeLater(new Runnable() {
		 
		@Override
		public void run() {
			JFrame frame = new JFrame();
			JScrollPane scrollPane = new JScrollPane(new DataInsertMoviePanel());
			frame.getContentPane().add(scrollPane);
			 
			frame.setSize(1000, 800);
			// make sure the program closes when X is clicked
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// display the window
			frame.setVisible(true);
		}
	 });
		 
	 }
}
