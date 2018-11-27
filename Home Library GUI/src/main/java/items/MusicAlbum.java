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
	 * @return the albumName
	 */
	public String getAlbumName() {
		return albumName;
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
	public Integer getYearPublished() {
		return yearPublished;
	}

	/**
	 * @param yearPublished the yearPublished to set
	 */
	public void setYearPublished(Integer yearPublished) {
		this.yearPublished = yearPublished;
	}

	/**
	 * @return the producer
	 */
	public Person getProducer() {
		return producer;
	}

	/**
	 * @param producer the producer to set
	 */
	public void setProducer(Person producer) {
		this.producer = producer;
	}

	/**
	 * @return the diskType
	 */
	public Integer getDiskType() {
		return diskType;
	}

	/**
	 * @param diskType the diskType to set
	 */
	public void setDiskType(Integer diskType) {
		this.diskType = diskType;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MusicAlbum [albumName=" + albumName + ", yearPublished=" + yearPublished + ", producer=" + producer
				+ ", diskType=" + diskType + ", musicTrackList=" + musicTrackList + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((albumName == null) ? 0 : albumName.hashCode());
		result = prime * result + ((diskType == null) ? 0 : diskType.hashCode());
		result = prime * result + ((musicTrackList == null) ? 0 : musicTrackList.hashCode());
		result = prime * result + ((producer == null) ? 0 : producer.hashCode());
		result = prime * result + ((yearPublished == null) ? 0 : yearPublished.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MusicAlbum other = (MusicAlbum) obj;
		if (albumName == null) {
			if (other.albumName != null)
				return false;
		} else if (!albumName.equals(other.albumName))
			return false;
		if (diskType == null) {
			if (other.diskType != null)
				return false;
		} else if (!diskType.equals(other.diskType))
			return false;
		if (musicTrackList == null) {
			if (other.musicTrackList != null)
				return false;
		} else if (!musicTrackList.equals(other.musicTrackList))
			return false;
		if (producer == null) {
			if (other.producer != null)
				return false;
		} else if (!producer.equals(other.producer))
			return false;
		if (yearPublished == null) {
			if (other.yearPublished != null)
				return false;
		} else if (!yearPublished.equals(other.yearPublished))
			return false;
		return true;
	}
	
	
}
