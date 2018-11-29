package gui.pages;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.HL_xiewen4;
import database.DatabaseConnectionBookApi;
import database.DatabaseConnectionMovieApi;
import database.DatabaseConnectionMusicAlbumApi;
import gui.functions.GeneralFunctions;

public class ViewFilterPanel extends GeneralFunctions {

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
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!bookCheckBox.isSelected() && !movieCheckBox.isSelected() && !musicAlbumCheckBox.isSelected()) {
					JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Please select at least one of the checkboxes.");
				
				} else if (textAreaToString(nameSearchBar) == null) {
					JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Name Search bar is empty.");
					
				} else if (textAreaToString(yearSearchBar) == null) {
					JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Release year Search bar is empty.");
				
				} else if (textAreaToInt(yearSearchBar) == null) {
					JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Release year must be a integer.");
					
				} else {
					performFilter();
				}
			}
		});
		
		return submitBtn;
	}
	
	/**
	 * Perform the action depending on the filters selected
	 * REQ: fields can not be empty
	 */
	private void performFilter() {
		String target = nameSearchBar.getText();
		int year = textAreaToInt(yearSearchBar);
		ArrayList<String> productNameList = new ArrayList<>();
		ArrayList<String> productTypeList = new ArrayList<>();
		ArrayList<String> personNameList = new ArrayList<>();
		ArrayList<String> productReleaseYear = new ArrayList<>();
		
		
		if (bookCheckBox.isSelected()) {
			HashMap<String, String> bookTitleToAuthorMap = DatabaseConnectionBookApi.getBookTitleToAuthorMap(target, year);
			
			for (String bookTitle : bookTitleToAuthorMap.keySet()) {
				String authorName = bookTitleToAuthorMap.get(bookTitle);
				
				productNameList.add(bookTitle);
				productTypeList.add("<B>");
				personNameList.add(authorName);
				productReleaseYear.add(String.valueOf(year));
			}
		}
		
		if (musicAlbumCheckBox.isSelected()) {
			HashMap<String, String> musicAlbumNameToSingerMap = DatabaseConnectionMusicAlbumApi.getMusicAlbumTitleToSingerMap(target, year);
			
			for (String albumTitle : musicAlbumNameToSingerMap.keySet()) {
				String singerName = musicAlbumNameToSingerMap.get(albumTitle);
				
				productNameList.add(albumTitle);
				productTypeList.add("<M>");
				personNameList.add(singerName);
				productReleaseYear.add(String.valueOf(year));
			}
		}
		
		if (movieCheckBox.isSelected()) {
			HashMap<String, String> movieNameToSingerMap = DatabaseConnectionMovieApi.getMovieTitleToDirectorMap(target, year);
			
			for (String movieTitle : movieNameToSingerMap.keySet()) {
				String directorName = movieNameToSingerMap.get(movieTitle);
				
				productNameList.add(movieTitle);
				productTypeList.add("<F>");
				personNameList.add(directorName);
				productReleaseYear.add(String.valueOf(year));
			}
		}
		
		HashMap<String, ArrayList<String>> table = new HashMap<>();
		table.put("Product's Name", productNameList);
		table.put("Type", productTypeList);
		table.put("Director/Singer/Author", personNameList);
		table.put("Release Year", productReleaseYear);
		
		HL_xiewen4.mainFrame.flipPageTo(new ViewFilterResultPanel(table));
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
