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
	}

	/**
	 * @return the musicName
	 */
	public String getMusicName() {
		String result = "NULL";
		if (this.musicName != null) {
			result = "'" + this.musicName + "'";
		}
		return result;
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
		String result = "NULL";
		if (this.language != null) {
			result = "'" + this.language + "'";
		}
		return result;
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
	
	
}
