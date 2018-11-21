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
import items.Music;
import items.MusicAlbum;
import items.Person;

public class DataUpdateMusicAlbumEditPanel extends DataInsertMusicPanel {

	private static final long serialVersionUID = 2122687382995893831L;
	
	private MusicAlbum oldMusicAlbum;
	
	public DataUpdateMusicAlbumEditPanel(MusicAlbum oldMusicAlbum) {
		super();
		this.oldMusicAlbum = oldMusicAlbum;
		 
		// Initialize all music album fields
		instantiateMusicPanel("Update Music:", this.createSubmitButton());
		
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
	
	/**
	 * Set up the field environment for the oldMusicAlbum data
	 * @param musicAlbum
	 */
	private void setUpFieldEnvirnment(MusicAlbum musicAlbum) {
		// Make sure there are enough music track fields
		for (int i = 1; i < musicAlbum.getMusicTrackList().size(); i++) {
			this.addMusicTrackBtn.doClick();
		}
	}
	
	/**
	 * Fill the MusicAlbum fields given
	 * @param musicAlbum
	 */
	private void fillMusicAlbumInfo(MusicAlbum musicAlbum) {
		// nameOfDisk
		this.nameOfDisk.setText(this.formatString(musicAlbum.getDiskType()));
		
		// yearPublished
		this.yearPublished.setText(this.formatString(musicAlbum.getYearPublished()));
		
		// producerSurname
		this.producerSurname.setText(this.formatString(musicAlbum.getProducer().getSurname()));
		
		// producerFirstName
		this.producerFirstName.setText(this.formatString(musicAlbum.getProducer().getFirstName()));
		
		// producerMiddleName
		this.producerMiddleName.setText(this.formatString(musicAlbum.getProducer().getMiddleName()));
		
		// musicTrackTable
		int i = 0;
		for (Music music : musicAlbum.getMusicTrackList()) {
			ArrayList<JTextArea> musicTextAreaList = this.musicTrackTable.get(i);
			
			// musicTrackName
			musicTextAreaList.get(0).setText(formatString(music.getMusicName()));
			
			// languageOfLyrics
			musicTextAreaList.get(1).setText(formatString(music.getLanguage()));
			
			if (music.getSingerList().size() >= 1) {
				// singer1Surname
				musicTextAreaList.get(2).setText(formatString(music.getSingerList().get(0).getSurname()));
				
				// singer1FirstName
				musicTextAreaList.get(3).setText(formatString(music.getSingerList().get(0).getFirstName()));
				
				// singer1MiddleName
				musicTextAreaList.get(4).setText(formatString(music.getSingerList().get(0).getMiddleName()));
			}
			
			if (music.getSingerList().size() >= 2) {
				// singer2Surname
				musicTextAreaList.get(5).setText(formatString(music.getSingerList().get(1).getSurname()));
				
				// singer2FirstName
				musicTextAreaList.get(6).setText(formatString(music.getSingerList().get(1).getFirstName()));
				
				// singer2MiddleName
				musicTextAreaList.get(7).setText(formatString(music.getSingerList().get(1).getMiddleName()));
			}
			
			// songWriterSurname
			musicTextAreaList.get(8).setText(formatString(music.getSongWriter().getSurname()));
			
			// songWriterFirstName
			musicTextAreaList.get(9).setText(formatString(music.getSongWriter().getFirstName()));
			
			// songWriterMiddleName
			musicTextAreaList.get(10).setText(formatString(music.getSongWriter().getMiddleName()));
			
			// composerSurname
			musicTextAreaList.get(11).setText(formatString(music.getComposer().getSurname()));
			
			// composerFirstName
			musicTextAreaList.get(12).setText(formatString(music.getComposer().getFirstName()));
			
			// composerMiddleName
			musicTextAreaList.get(13).setText(formatString(music.getComposer().getMiddleName()));
			
			// arrangerSurname
			musicTextAreaList.get(14).setText(formatString(music.getArranger().getSurname()));
			
			// arrangerFirstName
			musicTextAreaList.get(15).setText(formatString(music.getArranger().getFirstName()));
			
			// arrangerMiddleName
			musicTextAreaList.get(16).setText(formatString(music.getArranger().getMiddleName()));
			
			i = i + 1;
		}
	}
	
	private String formatString(String str) {
		return str.replaceAll("'", "").replaceAll("NULL", "").replaceAll("-1", "");
	}
}
