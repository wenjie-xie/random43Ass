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
		this.nameOfDisk.setText(musicAlbum.getAlbumName());
		
		// diskType
		this.diskType.setText(formatIntToStr(musicAlbum.getDiskType()));
		
		// yearPublished
		this.yearPublished.setText(formatIntToStr(musicAlbum.getYearPublished()));
		
		// producerSurname
		this.producerSurname.setText(musicAlbum.getProducer().getSurname());
		
		// producerFirstName
		this.producerFirstName.setText(musicAlbum.getProducer().getFirstName());
		
		// producerMiddleName
		this.producerMiddleName.setText(musicAlbum.getProducer().getMiddleName());
		
		// producerMiddleName
		this.producerGender.setText(formatIntToStr(musicAlbum.getProducer().getGender()));
		
		// musicTrackTable
		int i = 0;
		for (Music music : musicAlbum.getMusicTrackList()) {
			ArrayList<JTextArea> musicTextAreaList = this.musicTrackTable.get(i);
			
			// musicTrackName
			musicTextAreaList.get(0).setText(music.getMusicName());
			
			// languageOfLyrics
			musicTextAreaList.get(1).setText(music.getLanguage());
			
			// singer1Surname
			musicTextAreaList.get(2).setText(music.getSingerList().get(0).getSurname());
			
			// singer1FirstName
			musicTextAreaList.get(3).setText(music.getSingerList().get(0).getFirstName());
			
			// singer1MiddleName
			musicTextAreaList.get(4).setText(music.getSingerList().get(0).getMiddleName());
			
			// singer1Gender
			musicTextAreaList.get(5).setText(formatIntToStr(music.getSingerList().get(0).getGender()));
			
			if (music.getSingerList().size() >= 2) {
				// singer2Surname
				musicTextAreaList.get(6).setText(music.getSingerList().get(1).getSurname());
				
				// singer2FirstName
				musicTextAreaList.get(7).setText(music.getSingerList().get(1).getFirstName());
				
				// singer2MiddleName
				musicTextAreaList.get(8).setText(music.getSingerList().get(1).getMiddleName());
				
				// singer2Gender
				musicTextAreaList.get(9).setText(formatIntToStr(music.getSingerList().get(1).getGender()));
			}
			
			// songWriterSurname
			musicTextAreaList.get(10).setText(music.getSongWriter().getSurname());
			
			// songWriterFirstName
			musicTextAreaList.get(11).setText(music.getSongWriter().getFirstName());
			
			// songWriterMiddleName
			musicTextAreaList.get(12).setText(music.getSongWriter().getMiddleName());
			
			// songWriterGender
			musicTextAreaList.get(13).setText(formatIntToStr(music.getSongWriter().getGender()));
			
			// composerSurname
			musicTextAreaList.get(14).setText(music.getComposer().getSurname());
			
			// composerFirstName
			musicTextAreaList.get(15).setText(music.getComposer().getFirstName());
			
			// composerMiddleName
			musicTextAreaList.get(16).setText(music.getComposer().getMiddleName());
			
			// composerGender
			musicTextAreaList.get(17).setText(formatIntToStr(music.getComposer().getGender()));
			
			// arrangerSurname
			musicTextAreaList.get(18).setText(music.getArranger().getSurname());
			
			// arrangerFirstName
			musicTextAreaList.get(19).setText(music.getArranger().getFirstName());
			
			// arrangerMiddleName
			musicTextAreaList.get(20).setText(music.getArranger().getMiddleName());
			
			// arrangerGender
			musicTextAreaList.get(21).setText(formatIntToStr(music.getArranger().getGender()));
			
			i = i + 1;
		}
	}
}
