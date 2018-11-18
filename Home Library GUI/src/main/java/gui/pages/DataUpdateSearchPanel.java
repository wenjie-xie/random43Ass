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

/**
 * Data > Update
 * @author xiewen4
 *
 */
public class DataUpdateSearchPanel extends JPanel {

	private static final long serialVersionUID = -1914365473428451644L;
	
	public DataUpdateSearchPanel() {
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
		JTextArea searchField = new JTextArea();
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
			// Check if the target is a book, music album, movie or neither
			String target = DatabaseConnectionApi.verifyTargetName(targetName);
			
			switch (target) {
				case "Book":
					Book targetBook = DatabaseConnectionApi.getBook(targetName);
					// if it is a book navigate to the DataUpdateBookEditPanel
					HL_xiewen4.mainFrame.flipPageTo(new DataUpdateBookEditPanel(targetBook));
					break;
				case "Music Album":
					MusicAlbum targetMusicAlbum = DatabaseConnectionApi.getMusicAlbum(targetName);
					// if it is a music album navigate to the DataUpdateMusicAlbumEditPanel
					break;
				case "Movie":
					Movie targetMovie = DatabaseConnectionApi.getMovie(targetName);
					// If it is a movie navigate to the DataUpdateMovieEditPanel
					break;
				default:
					// If it is neither respond by showing a message dialog
					JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Invalid Name");
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Unable to connect to the database.");
		}
	}
	
}
