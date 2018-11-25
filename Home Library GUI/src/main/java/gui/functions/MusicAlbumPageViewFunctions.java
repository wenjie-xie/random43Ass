package gui.functions;

import java.util.ArrayList;

import javax.swing.JTextArea;

import items.Music;
import items.MusicAlbum;

public class MusicAlbumPageViewFunctions extends MusicAlbumPageInsertFunctions {

	private static final long serialVersionUID = -9025610753078339290L;

	/**
	 * Set up the field environment for the oldMusicAlbum data
	 * @param musicAlbum
	 */
	protected void setUpFieldEnvirnment(MusicAlbum musicAlbum) {
		// Make sure there are enough music track fields
		for (int i = 1; i < musicAlbum.getMusicTrackList().size(); i++) {
			this.addMusicTrackBtn.doClick();
		}
	}
	
	/**
	 * Fill the MusicAlbum fields given
	 * @param musicAlbum
	 */
	protected void fillMusicAlbumInfo(MusicAlbum musicAlbum) {
		// nameOfDisk
		this.nameOfDisk.setText(this.formatString(musicAlbum.getAlbumName()));
		
		// diskType
		this.diskType.setText(this.formatString(musicAlbum.getDiskType()));
		
		// yearPublished
		this.yearPublished.setText(this.formatString(musicAlbum.getYearPublished()));
		
		// producerSurname
		this.producerSurname.setText(this.formatString(musicAlbum.getProducer().getSurname()));
		
		// producerFirstName
		this.producerFirstName.setText(this.formatString(musicAlbum.getProducer().getFirstName()));
		
		// producerMiddleName
		this.producerMiddleName.setText(this.formatString(musicAlbum.getProducer().getMiddleName()));
		
		// producerMiddleName
		this.producerGender.setText(this.formatString(musicAlbum.getProducer().getGender()));
		
		// musicTrackTable
		int i = 0;
		for (Music music : musicAlbum.getMusicTrackList()) {
			ArrayList<JTextArea> musicTextAreaList = this.musicTrackTable.get(i);
			
			// musicTrackName
			musicTextAreaList.get(0).setText(formatString(music.getMusicName()));
			
			// languageOfLyrics
			musicTextAreaList.get(1).setText(formatString(music.getLanguage()));
			
			// singer1Surname
			musicTextAreaList.get(2).setText(formatString(music.getSingerList().get(0).getSurname()));
			
			// singer1FirstName
			musicTextAreaList.get(3).setText(formatString(music.getSingerList().get(0).getFirstName()));
			
			// singer1MiddleName
			musicTextAreaList.get(4).setText(formatString(music.getSingerList().get(0).getMiddleName()));
			
			// singer1Gender
			musicTextAreaList.get(5).setText(formatString(music.getSingerList().get(0).getGender()));
			
			if (music.getSingerList().size() >= 2) {
				// singer2Surname
				musicTextAreaList.get(6).setText(formatString(music.getSingerList().get(1).getSurname()));
				
				// singer2FirstName
				musicTextAreaList.get(7).setText(formatString(music.getSingerList().get(1).getFirstName()));
				
				// singer2MiddleName
				musicTextAreaList.get(8).setText(formatString(music.getSingerList().get(1).getMiddleName()));
				
				// singer2Gender
				musicTextAreaList.get(9).setText(formatString(music.getSingerList().get(1).getGender()));
			}
			
			// songWriterSurname
			musicTextAreaList.get(10).setText(formatString(music.getSongWriter().getSurname()));
			
			// songWriterFirstName
			musicTextAreaList.get(11).setText(formatString(music.getSongWriter().getFirstName()));
			
			// songWriterMiddleName
			musicTextAreaList.get(12).setText(formatString(music.getSongWriter().getMiddleName()));
			
			// songWriterGender
			musicTextAreaList.get(13).setText(formatString(music.getSongWriter().getGender()));
			
			// composerSurname
			musicTextAreaList.get(14).setText(formatString(music.getComposer().getSurname()));
			
			// composerFirstName
			musicTextAreaList.get(15).setText(formatString(music.getComposer().getFirstName()));
			
			// composerMiddleName
			musicTextAreaList.get(16).setText(formatString(music.getComposer().getMiddleName()));
			
			// composerGender
			musicTextAreaList.get(17).setText(formatString(music.getComposer().getGender()));
			
			// arrangerSurname
			musicTextAreaList.get(18).setText(formatString(music.getArranger().getSurname()));
			
			// arrangerFirstName
			musicTextAreaList.get(19).setText(formatString(music.getArranger().getFirstName()));
			
			// arrangerMiddleName
			musicTextAreaList.get(20).setText(formatString(music.getArranger().getMiddleName()));
			
			// arrangerGender
			musicTextAreaList.get(21).setText(formatString(music.getArranger().getGender()));
			
			i = i + 1;
		}
	}
	
	private static String formatString(String str) {
		
		String result = str.replaceAll("'", "");
		
		if (str.length() == 4) {
			result = result.replaceAll("NULL", "").replaceAll("null", "");
		}
		
		if (result.equals("")) {
			result = null;
		}
		return result;
	}
}
