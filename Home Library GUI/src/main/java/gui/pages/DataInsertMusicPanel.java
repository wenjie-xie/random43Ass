package gui.pages;

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

public class DataInsertMusicPanel extends JPanel {

	private static final long serialVersionUID = 5011747774458485854L;

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
}
