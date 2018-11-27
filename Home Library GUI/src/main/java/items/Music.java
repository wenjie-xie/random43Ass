/**
 * 
 */
package items;

import java.util.ArrayList;

/**
 * @author xiewen4
 * This is a object that stores information of a music track
 */
public class Music {
	private String musicName;
	private String language;
	private ArrayList<Person> singerList;
	private Person songWriter;
	private Person composer;
	private Person arranger;
	
	public Music(String musicName) {
		this.musicName = musicName;
		this.language = null;
		this.singerList = new ArrayList<>();
		this.songWriter = null;
		this.composer = null;
		this.arranger = null;
	}

	/**
	 * @return the musicName
	 */
	public String getMusicName() {
		return musicName;
	}

	/**
	 * @param musicName the musicName to set
	 */
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the singerList
	 */
	public ArrayList<Person> getSingerList() {
		return singerList;
	}

	/**
	 * @param singerList the singerList to set
	 */
	public void setSingerList(ArrayList<Person> singerList) {
		this.singerList = singerList;
	}

	/**
	 * @return the songWriter
	 */
	public Person getSongWriter() {
		return songWriter;
	}

	/**
	 * @param songWriter the songWriter to set
	 */
	public void setSongWriter(Person songWriter) {
		this.songWriter = songWriter;
	}

	/**
	 * @return the composer
	 */
	public Person getComposer() {
		return composer;
	}

	/**
	 * @param composer the composer to set
	 */
	public void setComposer(Person composer) {
		this.composer = composer;
	}

	/**
	 * @return the arranger
	 */
	public Person getArranger() {
		return arranger;
	}

	/**
	 * @param arranger the arranger to set
	 */
	public void setArranger(Person arranger) {
		this.arranger = arranger;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Music [musicName=" + musicName + ", language=" + language + ", singerList=" + singerList
				+ ", songWriter=" + songWriter + ", composer=" + composer + ", arranger=" + arranger + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arranger == null) ? 0 : arranger.hashCode());
		result = prime * result + ((composer == null) ? 0 : composer.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((musicName == null) ? 0 : musicName.hashCode());
		result = prime * result + ((singerList == null) ? 0 : singerList.hashCode());
		result = prime * result + ((songWriter == null) ? 0 : songWriter.hashCode());
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
		Music other = (Music) obj;
		if (arranger == null) {
			if (other.arranger != null)
				return false;
		} else if (!arranger.equals(other.arranger))
			return false;
		if (composer == null) {
			if (other.composer != null)
				return false;
		} else if (!composer.equals(other.composer))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (musicName == null) {
			if (other.musicName != null)
				return false;
		} else if (!musicName.equals(other.musicName))
			return false;
		if (singerList == null) {
			if (other.singerList != null)
				return false;
		} else if (!singerList.equals(other.singerList))
			return false;
		if (songWriter == null) {
			if (other.songWriter != null)
				return false;
		} else if (!songWriter.equals(other.songWriter))
			return false;
		return true;
	}

	
	
}
