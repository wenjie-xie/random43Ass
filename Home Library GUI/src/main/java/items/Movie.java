/**
 * 
 */
package items;

import java.util.ArrayList;

/**
 * @author xiewen4
 * This object is use to represent a movie in the database
 */
public class Movie {
	private String movieName;
	private ArrayList<Person> directorList;
	private ArrayList<Person> scriptWriterList;
	private ArrayList<Person> castList;
	private ArrayList<Person> producerList;
	private ArrayList<Person> composerList;
	private ArrayList<Person> editorList;
	private ArrayList<Person> costumeDesignerList;
	private Integer releaseYear;
	private Integer award;
	
	public Movie(String movieName, Integer releaseYear) {
		this.movieName = movieName;
		this.releaseYear = releaseYear;
		this.directorList = new ArrayList<>();
		this.scriptWriterList = new ArrayList<>();
		this.castList = new ArrayList<>();
		this.producerList = new ArrayList<>();
		this.composerList = new ArrayList<>();
		this.editorList = new ArrayList<>();
		this.costumeDesignerList = new ArrayList<>();
		this.award = null;
	}

	/**
	 * @return the movieName
	 */
	public String getMovieName() {
		return movieName;
	}

	/**
	 * @param movieName the movieName to set
	 */
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	/**
	 * @return the directorList
	 */
	public ArrayList<Person> getDirectorList() {
		return directorList;
	}

	/**
	 * @param directorList the directorList to set
	 */
	public void setDirectorList(ArrayList<Person> directorList) {
		this.directorList = directorList;
	}

	/**
	 * @return the scriptWriterList
	 */
	public ArrayList<Person> getScriptWriterList() {
		return scriptWriterList;
	}

	/**
	 * @param scriptWriterList the scriptWriterList to set
	 */
	public void setScriptWriterList(ArrayList<Person> scriptWriterList) {
		this.scriptWriterList = scriptWriterList;
	}

	/**
	 * @return the castList
	 */
	public ArrayList<Person> getCastList() {
		return castList;
	}

	/**
	 * @param castList the castList to set
	 */
	public void setCastList(ArrayList<Person> castList) {
		this.castList = castList;
	}

	/**
	 * @return the producerList
	 */
	public ArrayList<Person> getProducerList() {
		return producerList;
	}

	/**
	 * @param producerList the producerList to set
	 */
	public void setProducerList(ArrayList<Person> producerList) {
		this.producerList = producerList;
	}

	/**
	 * @return the composerList
	 */
	public ArrayList<Person> getComposerList() {
		return composerList;
	}

	/**
	 * @param composerList the composerList to set
	 */
	public void setComposerList(ArrayList<Person> composerList) {
		this.composerList = composerList;
	}

	/**
	 * @return the editorList
	 */
	public ArrayList<Person> getEditorList() {
		return editorList;
	}

	/**
	 * @param editorList the editorList to set
	 */
	public void setEditorList(ArrayList<Person> editorList) {
		this.editorList = editorList;
	}

	/**
	 * @return the costumeDesignerList
	 */
	public ArrayList<Person> getCostumeDesignerList() {
		return costumeDesignerList;
	}

	/**
	 * @param costumeDesignerList the costumeDesignerList to set
	 */
	public void setCostumeDesignerList(ArrayList<Person> costumeDesignerList) {
		this.costumeDesignerList = costumeDesignerList;
	}

	/**
	 * @return the releaseYear
	 */
	public Integer getReleaseYear() {
		return releaseYear;
	}

	/**
	 * @param releaseYear the releaseYear to set
	 */
	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	/**
	 * @return the award
	 */
	public Integer getAward() {
		return award;
	}

	/**
	 * @param award the award to set
	 */
	public void setAward(Integer award) {
		this.award = award;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Movie [movieName=" + movieName + ", directorList=" + directorList + ", scriptWriterList="
				+ scriptWriterList + ", castList=" + castList + ", producerList=" + producerList + ", composerList="
				+ composerList + ", editorList=" + editorList + ", costumeDesignerList=" + costumeDesignerList
				+ ", releaseYear=" + releaseYear + ", award=" + award + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((award == null) ? 0 : award.hashCode());
		result = prime * result + ((castList == null) ? 0 : castList.hashCode());
		result = prime * result + ((composerList == null) ? 0 : composerList.hashCode());
		result = prime * result + ((costumeDesignerList == null) ? 0 : costumeDesignerList.hashCode());
		result = prime * result + ((directorList == null) ? 0 : directorList.hashCode());
		result = prime * result + ((editorList == null) ? 0 : editorList.hashCode());
		result = prime * result + ((movieName == null) ? 0 : movieName.hashCode());
		result = prime * result + ((producerList == null) ? 0 : producerList.hashCode());
		result = prime * result + ((releaseYear == null) ? 0 : releaseYear.hashCode());
		result = prime * result + ((scriptWriterList == null) ? 0 : scriptWriterList.hashCode());
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
		Movie other = (Movie) obj;
		if (award == null) {
			if (other.award != null)
				return false;
		} else if (!award.equals(other.award))
			return false;
		if (castList == null) {
			if (other.castList != null)
				return false;
		} else if (!castList.equals(other.castList))
			return false;
		if (composerList == null) {
			if (other.composerList != null)
				return false;
		} else if (!composerList.equals(other.composerList))
			return false;
		if (costumeDesignerList == null) {
			if (other.costumeDesignerList != null)
				return false;
		} else if (!costumeDesignerList.equals(other.costumeDesignerList))
			return false;
		if (directorList == null) {
			if (other.directorList != null)
				return false;
		} else if (!directorList.equals(other.directorList))
			return false;
		if (editorList == null) {
			if (other.editorList != null)
				return false;
		} else if (!editorList.equals(other.editorList))
			return false;
		if (movieName == null) {
			if (other.movieName != null)
				return false;
		} else if (!movieName.equals(other.movieName))
			return false;
		if (producerList == null) {
			if (other.producerList != null)
				return false;
		} else if (!producerList.equals(other.producerList))
			return false;
		if (releaseYear == null) {
			if (other.releaseYear != null)
				return false;
		} else if (!releaseYear.equals(other.releaseYear))
			return false;
		if (scriptWriterList == null) {
			if (other.scriptWriterList != null)
				return false;
		} else if (!scriptWriterList.equals(other.scriptWriterList))
			return false;
		return true;
	}

	
	
}
