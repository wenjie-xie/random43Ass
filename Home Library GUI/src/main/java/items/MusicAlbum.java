package items;

import java.util.ArrayList;

/**
 * @author xiewen4
 * This object is use to represent a Music Album within the database
 */
public class MusicAlbum {
	private String albumName;
	private Integer yearPublished;
	private Person producer;
	private Integer diskType;
	private ArrayList<Music> musicTrackList;
	
	public MusicAlbum(String albumName, Integer yearPublished, ArrayList<Music> musicTrackList) {
		this.albumName = albumName;
		this.yearPublished = yearPublished;
		this.musicTrackList = musicTrackList;
		this.diskType = null;
		this.producer = null;
	}
	
	

	/**
	 * @return the diskType
	 */
	public String getDiskType() {
		String result = "NULL";
		if (this.diskType != null) {
			result = "" + this.diskType;
		}
		return result;
	}

	/**
	 * @return the diskType
	 */
	public Integer getDiskTypeInt() {
		return this.diskType;
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
		if (this.yearPublished != null) {
			result = "" + this.yearPublished;
		}
		return result;
	}
	
	/**
	 * @return the yearPublished
	 */
	public Integer getYearPublishedInt() {
		return this.yearPublished;
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
		return this.producer;
	}

	/**
	 * @param producer the producer to set
	 */
	public void setProducer(Person producer) {
		this.producer = producer;
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
