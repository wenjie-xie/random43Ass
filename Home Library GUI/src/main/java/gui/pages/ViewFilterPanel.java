package gui.pages;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.HL_xiewen4;

public class ViewFilterPanel extends JPanel {

	private static final long serialVersionUID = -5304227219476304214L;

	// Component
	private JCheckBox bookCheckBox;
	private JCheckBox musicAlbumCheckBox;
	private JCheckBox movieCheckBox;
	private JTextArea nameSearchBar;
	private JTextArea yearSearchBar;
	
	public ViewFilterPanel() {
		super();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 2;
		c.insets.bottom = 2;
		c.insets.left = 2;
		c.insets.right = 2;
		
		// Title
		JLabel message = new JLabel("Search Product To View:");
		message.setFont(new Font(message.getName(), Font.PLAIN, 30));
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(message, c);
		
		// Check Boxes
		bookCheckBox = new JCheckBox("Target Books");
		c.gridy = 2;
		c.gridx = 1;
		this.add(bookCheckBox, c);
		musicAlbumCheckBox = new JCheckBox("Target Music Albums");
		c.gridx = 2;
		this.add(musicAlbumCheckBox, c);
		movieCheckBox = new JCheckBox("Target Movies");
		c.gridx = 3;
		this.add(movieCheckBox, c);
		
		// Name search field
		JLabel nameSearchLabel = new JLabel("Search Name: ");
		c.gridy = 3;
		c.gridx = 1;
		this.add(nameSearchLabel, c);
		
		nameSearchBar = new JTextArea();
		c.gridx = 2;
		this.add(nameSearchBar, c);
		
		// Search year
		JLabel yearSearchLabel = new JLabel("Search Release Year: ");
		c.gridy = 4;
		c.gridx = 1;
		this.add(yearSearchLabel, c);
		
		yearSearchBar = new JTextArea();
		c.gridx = 2;
		this.add(yearSearchBar, c);
		
		// Buttons
		JButton submitBtn = createSubmitBtn();
		c.gridy = 5;
		c.gridx = 1;
		this.add(submitBtn, c);
		
		JButton cancelBtn = createCancelBtn();
		c.gridx = 2;
		this.add(cancelBtn, c);
	}
	
	/**
	 * Create a search button
	 * @return a search button
	 */
	private JButton createSubmitBtn() {
		JButton searchBtn = new JButton("Submit");
		searchBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		
		return searchBtn;
	}
	
	/**
	 * Create a cancel search button
	 * @return a cancel button
	 */
	private JButton createCancelBtn() {
		JButton searchBtn = new JButton("Cancel");
		searchBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
			}
		});
		
		return searchBtn;
	}
}
