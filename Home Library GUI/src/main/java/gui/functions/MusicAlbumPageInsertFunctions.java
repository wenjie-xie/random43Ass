package gui.functions;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.HL_xiewen4;
import gui.pages.HomePagePanel;
import items.Music;
import items.MusicAlbum;
import items.Person;

public class MusicAlbumPageInsertFunctions extends GeneralFunctions {

	private static final long serialVersionUID = -3882979294291666328L;

	protected JTextArea nameOfDisk;
	protected JTextArea diskType;
	protected JTextArea yearPublished;
	protected JTextArea producerSurname;
	protected JTextArea producerFirstName;
	protected JTextArea producerMiddleName;
	protected JTextArea producerGender;
	protected ArrayList<ArrayList<JTextArea>> musicTrackTable;
	protected JButton addMusicTrackBtn;
	
	/**
	 * Instantiate Music panel
	 */
	protected void instantiateMusicPanel(String title, JButton submitButton) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 2;
		c.insets.bottom = 2;
		c.insets.left = 2;
		c.insets.right = 2;
		
		// Add Music message
		JLabel message = new JLabel(title);
		message.setFont(new Font(message.getName(), Font.PLAIN, 30));
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(message, c);
		
		// Name of the Disk
		nameOfDisk = this.createTextField("Name of the Disk:", 1, 0);
		
		// Disk Type
		this.diskType = this.createTextField("Disk Type:", 2, 0);
		
		// Year Published
		yearPublished = this.createTextField("Year Published:", 3, 0);
		
		// Producer (1)
		producerSurname = this.createTextField("Producer Surname:", 4, 0);
		producerFirstName = this.createTextField("Producer First Name:", 4, 2);
		producerMiddleName = this.createTextField("Producer Middle Name:", 4, 4);
		producerGender = this.createTextField("Producer Gender:", 4, 6);
		
		// Add music track to the album
		musicTrackTable = new ArrayList<ArrayList<JTextArea>>();
		musicTrackTable.add(this.addOneMusicTrack(5, 1));
		int nextRowNum = musicTrackTable.get(0).size() + 5;
		
		this.addMusicTrackBtn = new JButton("Add Music Track");
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
		JButton submitBtn = submitButton;
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
				HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
				
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
		JTextArea singer1Gender = this.createTextField("Singer 1 Gender:", targetRow, 6);
		textAreaList.addAll(Arrays.asList(singer1Surname, singer1FirstName, singer1MiddleName, singer1Gender));
		
		targetRow = targetRow + 1;
		JTextArea singer2Surname = this.createTextField("Singer 2 Surname:", targetRow, 0);
		JTextArea singer2FirstName = this.createTextField("Singer 2 First Name:", targetRow, 2);
		JTextArea singer2MiddleName = this.createTextField("Singer 2 Middle Name:", targetRow, 4);
		JTextArea singer2Gender = this.createTextField("Singer 2 Gender:", targetRow, 6);
		textAreaList.addAll(Arrays.asList(singer2Surname, singer2FirstName, singer2MiddleName, singer2Gender));
		
		// Song Writer (1)
		targetRow = targetRow + 1;
		JTextArea songWriterSurname = this.createTextField("Song Writer Surname:", targetRow, 0);
		JTextArea songWriterFirstName = this.createTextField("Song Writer First Name:", targetRow, 2);
		JTextArea songWriterMiddleName = this.createTextField("Song Writer Middle Name:", targetRow, 4);
		JTextArea songWriterGender = this.createTextField("Song Writer Gender:", targetRow, 6);
		textAreaList.addAll(Arrays.asList(songWriterSurname, songWriterFirstName, songWriterMiddleName, songWriterGender));
		
		// Composer (1)
		targetRow = targetRow + 1;
		JTextArea composerSurname = this.createTextField("Composer Surname:", targetRow, 0);
		JTextArea composerFirstName = this.createTextField("Composer First Name:", targetRow, 2);
		JTextArea composerMiddleName = this.createTextField("Composer Middle Name:", targetRow, 4);
		JTextArea composerGender = this.createTextField("Composer Gender:", targetRow, 6);
		textAreaList.addAll(Arrays.asList(composerSurname, composerFirstName, composerMiddleName, composerGender));
		
		// Arranger (1)
		targetRow = targetRow + 1;
		JTextArea arrangerSurname = this.createTextField("Arranger Surname:", targetRow, 0);
		JTextArea arrangerFirstName = this.createTextField("Arranger First Name:", targetRow, 2);
		JTextArea arrangerMiddleName = this.createTextField("Arranger Middle Name:", targetRow, 4);
		JTextArea arrangerGender = this.createTextField("Arranger Gender:", targetRow, 6);
		textAreaList.addAll(Arrays.asList(arrangerSurname, arrangerFirstName, arrangerMiddleName, arrangerGender));
		
		return textAreaList;
	}
	
	
	/**
	 * Gather all the Music Album info into a MusicAlbum object
	 * @return musicAlbum object that contains all the info from the user
	 */
	protected MusicAlbum getMusicAlbumInfo() {
		// name of disk
		String targetAlbumName = textAreaToString(this.nameOfDisk);
		
		// disk type
		Integer targetDiskType = textAreaToInt(this.diskType);
		
		// year published
		Integer targetYearPublished = textAreaToInt(this.yearPublished);
		
		// music track list
		ArrayList<Music> musicList = new ArrayList<>();
		for (ArrayList<JTextArea> music : this.musicTrackTable) {
			// music track name
			String targetMusicName = textAreaToString(music.get(0));
			
			// Skip if the music name is blank
			if (targetMusicName != null) {
				// language of lyric
				String targetLanguage = textAreaToString(music.get(1));
				
				// singer1
				String targetSinger1Surname = textAreaToString(music.get(2));
				
				String targetSinger1FirstName = textAreaToString(music.get(3));
				
				String targetSinger1MiddleName = textAreaToString(music.get(4));
				
				Integer targetSinger1Gender = textAreaToInt(music.get(5));
				
				Person targetSinger1 = new Person(targetSinger1Surname, targetSinger1FirstName);
				targetSinger1.setMiddleName(targetSinger1MiddleName);
				targetSinger1.setGender(targetSinger1Gender);
				
				// singer2
				String targetSinger2Surname = textAreaToString(music.get(6));
				
				String targetSinger2FirstName = textAreaToString(music.get(7));
				
				String targetSinger2MiddleName = textAreaToString(music.get(8));
				
				Integer targetSinger2Gender = textAreaToInt(music.get(9));
				
				Person targetSinger2 = new Person(targetSinger2Surname, targetSinger2FirstName);
				targetSinger2.setMiddleName(targetSinger2MiddleName);
				targetSinger2.setGender(targetSinger2Gender);
				
				// song writer
				String targetSongWriterSurname = textAreaToString(music.get(10));
				
				String targetSongWriterFirstName = textAreaToString(music.get(11));
				
				String targetSongWriterMiddleName = textAreaToString(music.get(12));
				
				Integer targetSongWriterGender = textAreaToInt(music.get(13));
				
				Person targetSongWriter = new Person(targetSongWriterSurname, targetSongWriterFirstName);
				targetSongWriter.setMiddleName(targetSongWriterMiddleName);
				targetSongWriter.setGender(targetSongWriterGender);
				
				// composer
				String targetComposerSurname = textAreaToString(music.get(14));
				
				String targetComposerFirstName = textAreaToString(music.get(15));
				
				String targetComposerMiddleName = textAreaToString(music.get(16));
				
				Integer targetComposerGender = textAreaToInt(music.get(17));
				
				Person targetComposer = new Person(targetComposerSurname, targetComposerFirstName);
				targetComposer.setMiddleName(targetComposerMiddleName);
				targetComposer.setGender(targetComposerGender);
				
				// arranger
				String targetArrangerSurname = textAreaToString(music.get(18));
				
				String targetArrangerFirstName = textAreaToString(music.get(19));
				
				String targetArrangerMiddleName = textAreaToString(music.get(20));
				
				Integer targetArrangerGender = textAreaToInt(music.get(21));
				
				Person targetArranger = new Person(targetArrangerSurname, targetArrangerFirstName);
				targetArranger.setMiddleName(targetArrangerMiddleName);
				targetArranger.setGender(targetArrangerGender);
				
				// Create a music object
				Music targetMusic = new Music(targetMusicName);
				targetMusic.setLanguage(targetLanguage);
				if ((this.formatString(targetSinger1.getFirstName()) != null) && (this.formatString(targetSinger1.getSurname()) != null)) {
					targetMusic.getSingerList().add(targetSinger1);
				}
				if ((this.formatString(targetSinger2.getFirstName()) != null) && (this.formatString(targetSinger2.getSurname()) != null)) {
					targetMusic.getSingerList().add(targetSinger2);
				}
				targetMusic.setSongWriter(targetSongWriter);
				targetMusic.setComposer(targetComposer);
				targetMusic.setArranger(targetArranger);
				
				// Add music
				musicList.add(targetMusic);
			} // SKIP
		}
		// Create a MusicAlbum object
		MusicAlbum musicAlbum = new MusicAlbum(targetAlbumName, targetYearPublished, musicList);
		musicAlbum.setDiskType(targetDiskType);
		
		// producer
		String targetProducerSurname = textAreaToString(this.producerSurname);
		
		String targetProducerFirstName = textAreaToString(this.producerFirstName);
		
		String targetProducerMiddleName = textAreaToString(this.producerMiddleName);
		
		Integer targetProducerGender = textAreaToInt(this.producerGender);
		
		Person targetProducer = new Person(targetProducerSurname, targetProducerFirstName);
		targetProducer.setMiddleName(targetProducerMiddleName);
		targetProducer.setGender(targetProducerGender);
		
		// Create a MusicAlbum object
		musicAlbum.setProducer(targetProducer);
		
		return musicAlbum;
	}
	
	private String formatString(String str) {
		String result = null;
		if (str != null) {
			result = str.replaceAll("'", "");
			
			if (str.length() == 4) {
				result = result.replaceAll("NULL", "").replaceAll("null", "");
			}
			
			if (result.equals("")) {
				result = null;
			}
		}
		return result;
	}
}
