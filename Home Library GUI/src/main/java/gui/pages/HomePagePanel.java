/**
 * 
 */
package gui.pages;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author xiewen4
 * This is the panel that should be shown when the Home Library
 * application is first opened.
 */
public class HomePagePanel extends JPanel {

	private static final long serialVersionUID = -3058262331437101754L;

	public HomePagePanel() {
		super();
		
		// Welcome message
		JLabel welcomeMessage = new JLabel("Welcome to Personal Home Library!");
		welcomeMessage.setFont(new Font(welcomeMessage.getName(), Font.PLAIN, 45));
		this.add(welcomeMessage);
	}
}
