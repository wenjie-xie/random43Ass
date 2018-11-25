package gui.pages;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.HL_xiewen4;
import database.DatabaseConnectionMusicAlbumApi;
import gui.functions.MusicAlbumPageViewFunctions;
import items.Music;
import items.MusicAlbum;
import items.Person;

public class DataUpdateMusicAlbumEditPanel extends MusicAlbumPageViewFunctions {

	private static final long serialVersionUID = 2122687382995893831L;
	
	private MusicAlbum oldMusicAlbum;
	
	public DataUpdateMusicAlbumEditPanel(MusicAlbum oldMusicAlbum) {
		this.oldMusicAlbum = oldMusicAlbum;
		
		// Initialize panel
		instantiateMusicPanel("Update Music Album:", this.createSubmitButton());
		
		// Set up field
		setUpFieldEnvirnment(oldMusicAlbum);
		
		// fill in fields
		fillMusicAlbumInfo(oldMusicAlbum);
	}
	
	
	/**
	 * Create a submit button for this panel
	 * @return a submit JButton
	 */
	private JButton createSubmitButton() {
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Add all the info into the Music Album object
				MusicAlbum newMusicAlbum = getMusicAlbumInfo();
				
				try {
					DatabaseConnectionMusicAlbumApi.compareAndUpdateMusicAlbum(newMusicAlbum, oldMusicAlbum);
					
					HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
				} catch (NumberFormatException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		return submitBtn;
	}
	
}
