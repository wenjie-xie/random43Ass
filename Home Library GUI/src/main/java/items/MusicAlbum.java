package items;

import java.util.ArrayList;

/**
 * @author xiewen4
 * This object is use to represent a Music Album within the database
 */
public class MusicAlbum {
	private String albumName;
	private int yearPublished;
	private Person Producer;
	private int diskType;
	private ArrayList<Music> musicTrackList;
	
	public MusicAlbum(String albumName, int yearPublished, ArrayList<Music> musicTrackList) {
		this.albumName = albumName;
		this.yearPublished = yearPublished;
		this.musicTrackList = musicTrackList;
		this.diskType = -1;
	}
	
	

	/**
	 * @return the diskType
	 */
	public String getDiskType() {
		String result = "NULL";
		if (this.diskType != -1) {
			result = "" + this.diskType;
		}
		return result;
	}



	/**
	 * @param diskType the diskType to set
	 */
	public void setDiskType(int diskType) {
		this.diskType = diskType;
	}



	/**
	 * @return the albumName
	 */
	public String getAlbumName() {
		String result = "NULL";
		if (this.albumName != null) {
			result = "'" + this.albumName + "'";
		}
		return result;
	}

	/**
	 * @param albumName the albumName to set
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	/**
	 * @return the yearPublished
	 */
	public String getYearPublished() {
		String result = "NULL";
		if (this.yearPublished != -1) {
			result = "" + this.yearPublished;
		}
		return result;
	}

	/**
	 * @param yearPublished the yearPublished to set
	 */
	public void setYearPublished(int yearPublished) {
		this.yearPublished = yearPublished;
	}

	/**
	 * @return the producer
	 */
	public Person getProducer() {
		return Producer;
	}

	/**
	 * @param producer the producer to set
	 */
	public void setProducer(Person producer) {
		Producer = producer;
	}

	/**
	 * @return the musicTrackList
	 */
	public ArrayList<Music> getMusicTrackList() {
		return musicTrackList;
	}

	/**
	 * @param musicTrackList the musicTrackList to set
	 */
	public void setMusicTrackList(ArrayList<Music> musicTrackList) {
		this.musicTrackList = musicTrackList;
	}
	
}
