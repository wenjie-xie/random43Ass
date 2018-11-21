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

public class DataUpdateMusicAlbumEditPanel extends JPanel {

	private static final long serialVersionUID = 2122687382995893831L;

	private JTextArea nameOfDisk;
	private JTextArea yearPublished;
	private JTextArea producerSurname;
	private JTextArea producerFirstName;
	private JTextArea producerMiddleName;
	private ArrayList<ArrayList<JTextArea>> musicTrackTable;
	private JButton addMusicTrackBtn;
	
	private MusicAlbum oldMusicAlbum;
	
	public DataUpdateMusicAlbumEditPanel(MusicAlbum oldMusicAlbum) {
		super();
		this.oldMusicAlbum = oldMusicAlbum;
		 
		// Initialize all music album fields
		initalizeMusicAlbumFields();
		
		// Set up field
		setUpFieldEnvirnment(oldMusicAlbum);
		
		// fill in fields
		fillMusicAlbumInfo(oldMusicAlbum);
	}
	
	private void initalizeMusicAlbumFields() {
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
		this.add(this.addMusicTrackBtn, c);
		
		// Submit
		JButton submitBtn = createSubmitButton();
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
	 * Gather all the Music Album info into a MusicAlbum object
	 * @return musicAlbum object that contains all the info from the user
	 */
	private MusicAlbum getMusicAlbumInfo() {
		// name of disk
		String targetAlbumName = textAreaToString(this.nameOfDisk);
		
		// year published
		int targetYearPublished = textAreaToInt(this.yearPublished);
		
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
				
				Person targetSinger1 = new Person(targetSinger1Surname, targetSinger1FirstName);
				targetSinger1.setMiddleName(targetSinger1MiddleName);
				
				// singer2
				String targetSinger2Surname = textAreaToString(music.get(5));
				
				String targetSinger2FirstName = textAreaToString(music.get(6));
				
				String targetSinger2MiddleName = textAreaToString(music.get(7));
				
				Person targetSinger2 = new Person(targetSinger2Surname, targetSinger2FirstName);
				targetSinger2.setMiddleName(targetSinger2MiddleName);
				
				// song writer
				String targetSongWriterSurname = textAreaToString(music.get(8));
				
				String targetSongWriterFirstName = textAreaToString(music.get(9));
				
				String targetSongWriterMiddleName = textAreaToString(music.get(10));
				
				Person targetSongWriter = new Person(targetSongWriterSurname, targetSongWriterFirstName);
				targetSongWriter.setMiddleName(targetSongWriterMiddleName);
				
				// composer
				String targetComposerSurname = textAreaToString(music.get(11));
				
				String targetComposerFirstName = textAreaToString(music.get(12));
				
				String targetComposerMiddleName = textAreaToString(music.get(13));
				
				Person targetComposer = new Person(targetComposerSurname, targetComposerFirstName);
				targetComposer.setMiddleName(targetComposerMiddleName);
				
				// arranger
				String targetArrangerSurname = textAreaToString(music.get(14));
				
				String targetArrangerFirstName = textAreaToString(music.get(15));
				
				String targetArrangerMiddleName = textAreaToString(music.get(16));
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
			} // SKIP
		}
		// Create a MusicAlbum object
		MusicAlbum musicAlbum = new MusicAlbum(targetAlbumName, targetYearPublished, musicList);
		
		// producer
		String targetProducerSurname = textAreaToString(this.producerSurname);
		
		String targetProducerFirstName = textAreaToString(this.producerFirstName);
		
		String targetProducerMiddleName = textAreaToString(this.producerMiddleName);
		
		Person targetProducer = new Person(targetProducerSurname, targetProducerFirstName);
		targetProducer.setMiddleName(targetProducerMiddleName);
		
		// Create a MusicAlbum object
		musicAlbum.setProducer(targetProducer);
		
		return musicAlbum;
	}
	
	/**
	 * Get data from the text area as int
	 * @param textArea
	 * @return a int given, otherwise return -1
	 */
	private int textAreaToInt(JTextArea textArea) {
		int target;
		try {
			target = Integer.parseInt(textArea.getText());
		} catch (Exception e) {
			target = -1;
		}
		return target;
	}
	
	/**
	 * Get data from the text area as String
	 * @param textArea
	 * @return a String given, otherwise return null
	 */
	private String textAreaToString(JTextArea textArea) {
		String target;
		try {
			target = textArea.getText();
		} catch (NullPointerException e) {
			target = null;
		}
		return target;
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
