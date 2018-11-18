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
import database.DatabaseConnectionBookApi;
import database.DatabaseConnectionMusicAlbumApi;
import items.Book;
import items.Music;
import items.MusicAlbum;
import items.Person;

public class DataInsertMusicPanel extends JPanel {

	private static final long serialVersionUID = 5011747774458485854L;

	private JTextArea nameOfDisk;
	private JTextArea yearPublished;
	private JTextArea producerSurname;
	private JTextArea producerFirstName;
	private JTextArea producerMiddleName;
	private ArrayList<ArrayList<JTextArea>> musicTrackTable;
	
	public DataInsertMusicPanel() {
		super();
		 
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 2;
		c.insets.bottom = 2;
		c.insets.left = 2;
		c.insets.right = 2;
		
		// Add Music message
		JLabel message = new JLabel("Add New Music:");
		message.setFont(new Font(message.getName(), Font.PLAIN, 30));
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(message, c);
		
		// Name of the Disk
		JTextArea nameOfDisk = this.createTextField("Name of the Disk:", 1, 0);
		
		// Year Published
		JTextArea yearPublished = this.createTextField("Year Published:", 2, 0);
		
		// Producer (1)
		JTextArea producerSurname = this.createTextField("Producer Surname:", 3, 0);
		JTextArea producerFirstName = this.createTextField("Producer First Name:", 3, 2);
		JTextArea producerMiddleName = this.createTextField("Producer Middle Name:", 3, 4);
		
		// Add music track to the album
		ArrayList<ArrayList<JTextArea>> musicTrackTable = new ArrayList<ArrayList<JTextArea>>();
		musicTrackTable.add(this.addOneMusicTrack(4, 1));
		int nextRowNum = musicTrackTable.get(0).size() + 3;
		
		JButton addMusicTrackBtn = new JButton("Add Music Track");
		addMusicTrackBtn.addActionListener(new ActionListener() {
			private int rowStart = nextRowNum;
			private int trackNum = 2;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				musicTrackTable.add(addOneMusicTrack(nextRowNum + rowStart, trackNum));
				// The actual is a lot less
				this.rowStart = this.rowStart + musicTrackTable.get(0).size();
				trackNum = trackNum + 1;
				
				revalidate();
				repaint();
			}
		});
		c.gridy = 998;
		this.add(addMusicTrackBtn, c);
		
		// Submit
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 999;
		c.weightx = 0.5;
		this.add(submitBtn, c);
		
		// Cancel
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		c.gridx = 1;
		this.add(cancelBtn, c);
	}
	
	/**
	 * The purpose of this method is to create a text field for each given fieldName
	 * @param fieldName
	 * @param rowNum
	 * @param colNum
	 * @param showTextArea weather or not show text area
	 * @return
	 */
	private JTextArea createTextField(String fieldName, int rowNum, int colNum, boolean showTextArea) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 2;
		c.insets.bottom = 2;
		c.insets.left = 2;
		c.insets.right = 2;
		
		JLabel name = new JLabel(fieldName);
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = colNum;
		c.gridy = rowNum;
		this.add(name, c);
		
		if (showTextArea) {
			JTextArea field = new JTextArea();
			c.weightx = 1;
			c.gridx = colNum + 1;
			this.add(field, c);
			
			return field;
		}
		
		return null;
	}
	
	private JTextArea createTextField(String fieldName, int rowNum, int colNum) {
		return createTextField(fieldName, rowNum, colNum, true);
	}
	
	
	/**
	 * The purpose of this method is to add the fields for a new music track
	 * @param startRowNum
	 * @param musicTrackNum
	 * @return info for a music track
	 */
	private ArrayList<JTextArea> addOneMusicTrack(int startRowNum, int musicTrackNum) {
		ArrayList<JTextArea> textAreaList = new ArrayList<>();

		int targetRow = startRowNum;
		this.createTextField("********** Track #" + musicTrackNum + " ***********", targetRow, 0, false);
		
		// Name of the Music Track
		targetRow = targetRow + 1;
		JTextArea musicTrackName = this.createTextField("Name of the Music Track:", targetRow, 0);
		textAreaList.add(musicTrackName);
		
		// Language of the Lyrics
		targetRow = targetRow + 1;
		JTextArea languageOfLyrics = this.createTextField("Language of the Lyrics:", targetRow, 0);
		textAreaList.add(languageOfLyrics);
		
		// Singer (1-2)
		targetRow = targetRow + 1;
		JTextArea singer1Surname = this.createTextField("Singer 1 Surname:", targetRow, 0);
		JTextArea singer1FirstName = this.createTextField("Singer 1 First Name:", targetRow, 2);
		JTextArea singer1MiddleName = this.createTextField("Singer 1 Middle Name:", targetRow, 4);
		textAreaList.addAll(Arrays.asList(singer1Surname, singer1FirstName, singer1MiddleName));
		
		targetRow = targetRow + 1;
		JTextArea singer2Surname = this.createTextField("Singer 2 Surname:", targetRow, 0);
		JTextArea singer2FirstName = this.createTextField("Singer 2 First Name:", targetRow, 2);
		JTextArea singer2MiddleName = this.createTextField("Singer 2 Middle Name:", targetRow, 4);
		textAreaList.addAll(Arrays.asList(singer2Surname, singer2FirstName, singer2MiddleName));
		
		// Song Writer (1)
		targetRow = targetRow + 1;
		JTextArea songWriterSurname = this.createTextField("Song Writer Surname:", targetRow, 0);
		JTextArea songWriterFirstName = this.createTextField("Song Writer First Name:", targetRow, 2);
		JTextArea songWriterMiddleName = this.createTextField("Song Writer Middle Name:", targetRow, 4);
		textAreaList.addAll(Arrays.asList(songWriterSurname, songWriterFirstName, songWriterMiddleName));
		
		// Composer (1)
		targetRow = targetRow + 1;
		JTextArea composerSurname = this.createTextField("Composer Surname:", targetRow, 0);
		JTextArea composerFirstName = this.createTextField("Composer First Name:", targetRow, 2);
		JTextArea composerMiddleName = this.createTextField("Composer Middle Name:", targetRow, 4);
		textAreaList.addAll(Arrays.asList(composerSurname, composerFirstName, composerMiddleName));
		
		// Arranger (1)
		targetRow = targetRow + 1;
		JTextArea arrangerSurname = this.createTextField("Arranger Surname:", targetRow, 0);
		JTextArea arrangerFirstName = this.createTextField("Arranger First Name:", targetRow, 2);
		JTextArea arrangerMiddleName = this.createTextField("Arranger Middle Name:", targetRow, 4);
		textAreaList.addAll(Arrays.asList(arrangerSurname, arrangerFirstName, arrangerMiddleName));
		
		return textAreaList;
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
				
				try {
					DatabaseConnectionMusicAlbumApi.insertMusicAlbum(musicAlbum);
					HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		return submitBtn;
	}
	
	/**
	 * Gather all the Music Album info into a MusicAlbum object
	 * @return musicAlbum object that contains all the info from the user
	 */
	private MusicAlbum getMusicAlbumInfo() {
		// name of disk
		String targetAlbumName;
		try {
			targetAlbumName = this.nameOfDisk.getText();
		} catch (NullPointerException e) {
			targetAlbumName = null;
		}
		
		// year published
		int targetYearPublished;
		try {
			targetYearPublished = Integer.parseInt(this.yearPublished.getText());
		} catch (Exception e) {
			targetYearPublished = -1;
		}
		
		// music track list
		ArrayList<Music> musicList = new ArrayList<>();
		for (ArrayList<JTextArea> music : this.musicTrackTable) {
			// music track name
			String targetMusicName;
			try {
				targetMusicName = music.get(0).getText();
			} catch (NullPointerException e) {
				targetMusicName = null;
			}
			
			// language of lyric
			String targetLanguage;
			try {
				targetLanguage = music.get(1).getText();
			} catch (NullPointerException e) {
				targetLanguage = null;
			}
			
			// singer1
			String targetSinger1Surname;
			try {
				targetSinger1Surname = music.get(2).getText();
			} catch (NullPointerException e) {
				targetSinger1Surname = null;
			}
			
			String targetSinger1FirstName;
			try {
				targetSinger1FirstName = music.get(3).getText();
			} catch (NullPointerException e) {
				targetSinger1FirstName = null;
			}
			
			String targetSinger1MiddleName;
			try {
				targetSinger1MiddleName = music.get(4).getText();
			} catch (NullPointerException e) {
				targetSinger1MiddleName = null;
			}
			Person targetSinger1 = new Person(targetSinger1Surname, targetSinger1FirstName);
			targetSinger1.setMiddleName(targetSinger1MiddleName);
			
			// singer2
			String targetSinger2Surname;
			try {
				targetSinger2Surname = music.get(5).getText();
			} catch (NullPointerException e) {
				targetSinger2Surname = null;
			}
			
			String targetSinger2FirstName;
			try {
				targetSinger2FirstName = music.get(6).getText();
			} catch (NullPointerException e) {
				targetSinger2FirstName = null;
			}
			
			String targetSinger2MiddleName;
			try {
				targetSinger2MiddleName = music.get(7).getText();
			} catch (NullPointerException e) {
				targetSinger2MiddleName = null;
			}
			Person targetSinger2 = new Person(targetSinger2Surname, targetSinger2FirstName);
			targetSinger2.setMiddleName(targetSinger2MiddleName);
			
			// song writer
			String targetSongWriterSurname;
			try {
				targetSongWriterSurname = music.get(8).getText();
			} catch (NullPointerException e) {
				targetSongWriterSurname = null;
			}
			
			String targetSongWriterFirstName;
			try {
				targetSongWriterFirstName = music.get(9).getText();
			} catch (NullPointerException e) {
				targetSongWriterFirstName = null;
			}
			
			String targetSongWriterMiddleName;
			try {
				targetSongWriterMiddleName = music.get(10).getText();
			} catch (NullPointerException e) {
				targetSongWriterMiddleName = null;
			}
			Person targetSongWriter = new Person(targetSongWriterSurname, targetSongWriterFirstName);
			targetSongWriter.setMiddleName(targetSongWriterMiddleName);
			
			// composer
			String targetComposerSurname;
			try {
				targetComposerSurname = music.get(8).getText();
			} catch (NullPointerException e) {
				targetComposerSurname = null;
			}
			
			String targetComposerFirstName;
			try {
				targetComposerFirstName = music.get(9).getText();
			} catch (NullPointerException e) {
				targetComposerFirstName = null;
			}
			
			String targetComposerMiddleName;
			try {
				targetComposerMiddleName = music.get(10).getText();
			} catch (NullPointerException e) {
				targetComposerMiddleName = null;
			}
			Person targetComposer = new Person(targetComposerSurname, targetComposerFirstName);
			targetComposer.setMiddleName(targetComposerMiddleName);
			
			// arranger
			String targetArrangerSurname;
			try {
				targetArrangerSurname = music.get(8).getText();
			} catch (NullPointerException e) {
				targetArrangerSurname = null;
			}
			
			String targetArrangerFirstName;
			try {
				targetArrangerFirstName = music.get(9).getText();
			} catch (NullPointerException e) {
				targetArrangerFirstName = null;
			}
			
			String targetArrangerMiddleName;
			try {
				targetArrangerMiddleName = music.get(10).getText();
			} catch (NullPointerException e) {
				targetArrangerMiddleName = null;
			}
			Person targetArranger = new Person(targetArrangerSurname, targetArrangerFirstName);
			targetArranger.setMiddleName(targetArrangerMiddleName);
			
			// Create a music object
			Music targetMusic = new Music(targetMusicName);
			targetMusic.setLanguage(targetLanguage);
			targetMusic.setSingerList(new ArrayList<>(Arrays.asList(targetSinger1, targetSinger2)));
			targetMusic.setSongWriter(targetSongWriter);
			targetMusic.setComposer(targetComposer);
			targetMusic.setArranger(targetArranger);
			
			// Add music
			musicList.add(targetMusic);
		}
		// Create a MusicAlbum object
		MusicAlbum musicAlbum = new MusicAlbum(targetAlbumName, targetYearPublished, musicList);
		
		// producer
		String targetProducerSurname;
		try {
			targetProducerSurname = this.producerSurname.getText();
		} catch (NullPointerException e) {
			targetProducerSurname = null;
		}
		
		String targetProducerFirstName;
		try {
			targetProducerFirstName = this.producerFirstName.getText();
		} catch (NullPointerException e) {
			targetProducerFirstName = null;
		}
		
		String targetProducerMiddleName;
		try {
			targetProducerMiddleName = this.producerMiddleName.getText();
		} catch (NullPointerException e) {
			targetProducerMiddleName = null;
		}
		Person targetProducer = new Person(targetProducerSurname, targetProducerFirstName);
		targetProducer.setMiddleName(targetProducerMiddleName);
		
		// Create a MusicAlbum object
		musicAlbum.setProducer(targetProducer);
		
		return musicAlbum;
	}
}
