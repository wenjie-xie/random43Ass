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
import gui.functions.MusicAlbumPageInsertFunctions;
import items.Music;
import items.MusicAlbum;
import items.Person;

public class DataInsertMusicPanel extends MusicAlbumPageInsertFunctions {

	private static final long serialVersionUID = 5011747774458485854L;
	
	public DataInsertMusicPanel() {
		instantiateMusicPanel("Add New Music Album:", this.createSubmitButton());
		
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
				MusicAlbum musicAlbum = getMusicAlbumInfo();
				
				DatabaseConnectionMusicAlbumApi.insertMusicAlbum(musicAlbum);
				HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
				
			}
		});
		
		return submitBtn;
	}
}
