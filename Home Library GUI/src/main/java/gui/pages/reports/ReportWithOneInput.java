package gui.pages.reports;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public abstract class ReportWithOneInput extends JPanel {

	private static final long serialVersionUID = 6166185463419922185L;
	
	protected JTextArea textField;
	
	public ReportWithOneInput(String title, String fieldName) {
		super();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 2;
		c.insets.bottom = 2;
		c.insets.left = 2;
		c.insets.right = 2;
		
		// Title
		JLabel message = new JLabel(title);
		message.setFont(new Font(message.getName(), Font.PLAIN, 30));
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(message, c);
		
		JLabel nameLabel = new JLabel(fieldName);
		c.gridy = 1;
		c.gridx = 1;
		this.add(nameLabel, c);
		
		this.textField = new JTextArea();
		c.gridx = 2;
		this.add(textField, c);
		
		JButton submitBtn = createSubmitBtn();
		c.gridy = 2;
		c.gridx = 2;
		this.add(submitBtn, c);
	}
	
	protected abstract JButton createSubmitBtn();

}
