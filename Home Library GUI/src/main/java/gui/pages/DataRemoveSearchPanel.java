package gui.pages;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.HL_xiewen4;
import database.DatabaseConnectionBookApi;
import database.DatabaseConnectionMovieApi;
import database.DatabaseConnectionMusicAlbumApi;
import items.Book;
import items.Movie;
import items.MusicAlbum;

public class DataRemoveSearchPanel extends JPanel {
	private static final long serialVersionUID = 7628050289462881748L;
	
	private JTextArea searchField;
	
	public DataRemoveSearchPanel() {
		super();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 2;
		c.insets.bottom = 2;
		c.insets.left = 2;
		c.insets.right = 2;
		
		// Add Book message
		JLabel message = new JLabel("Search Product Name:");
		message.setFont(new Font(message.getName(), Font.PLAIN, 30));
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(message, c);
		
		// A description
		JLabel description = new JLabel("(Product Name => Name of the Book, Music Album or Movie)");
		c.gridy = 1;
		this.add(description, c);
		
		// Search Field
		searchField = new JTextArea();
		c.gridy = 2;
		this.add(searchField, c);
		
		// OK button
		JButton okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				okBtnAction(searchField.getText());
			}
		});
		c.gridy = 3;
		this.add(okBtn, c);
	}
	
	
	/**
	 * The action being performed by clicking the OK button to execute search
	 */
	private void okBtnAction(String targetName) {
		
		try {
			String id = null;
			// Check if the target is a book, music album, movie or neither
			if ((id = DatabaseConnectionBookApi.tryToFindBookName(targetName)) != null) {
				Book targetBook = DatabaseConnectionBookApi.getBookInfo(targetName);
				
				int targetOption = JOptionPane.showConfirmDialog(HL_xiewen4.mainFrame,
						"Are you sure you want to remove the Book <" + targetName + "> from library?");
				if (targetOption == JOptionPane.OK_OPTION) {
					// Remove
					DatabaseConnectionBookApi.removeBook(id);
				}
				
				// if it is a book navigate to the DataUpdateBookEditPanel
				HL_xiewen4.mainFrame.flipPageTo(new DataUpdateBookEditPanel(targetBook));
				this.searchField.setText("");
				
			} else if ((id = DatabaseConnectionMusicAlbumApi.tryToFindMusicAlbumName(targetName)) != null) {
				MusicAlbum targetMusicAlbum = DatabaseConnectionMusicAlbumApi.getMusicAlbumInfo(targetName);
				
				int targetOption = JOptionPane.showConfirmDialog(HL_xiewen4.mainFrame,
						"Are you sure you want to remove the Music Album <" + targetName + "> from library?");
				if (targetOption == JOptionPane.OK_OPTION) {
					// Remove
					DatabaseConnectionMusicAlbumApi.removeMusicAlbum(id);
				}
				
				// if it is a music album navigate to the DataUpdateMusicAlbumEditPanel
				HL_xiewen4.mainFrame.flipPageTo(new DataUpdateMusicAlbumEditPanel(targetMusicAlbum));
				this.searchField.setText("");
				
			} else if ((id = DatabaseConnectionMovieApi.tryToFindMovieName(targetName)) != null) {
				Movie targetMovie = DatabaseConnectionMovieApi.getMovieInfo(targetName);
				// If it is a movie
				
				int targetOption = JOptionPane.showConfirmDialog(HL_xiewen4.mainFrame,
						"Are you sure you want to remove the Movie <" + targetName + "> from library?");
				if (targetOption == JOptionPane.OK_OPTION) {
					// Remove
					DatabaseConnectionMovieApi.removeMovie(id);
				}
				
				this.searchField.setText("");
				
			} else {
				JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Item Not Found.");
			}
			
		} catch (SQLException e) {
			// JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Unable to connect to the database.");
			e.printStackTrace();
		}
	}
}
