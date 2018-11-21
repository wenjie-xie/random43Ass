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
		String result = "NULL";
		if (this.movieName != null) {
			result = "'" + this.movieName + "'";
		}
		return result;
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
	
	public void addDirector(Person newDirector) {
		this.directorList.add(newDirector);
	}

	/**
	 * @return the scriptWriter
	 */
	public ArrayList<Person> getScriptWriterList() {
		return scriptWriterList;
	}

	/**
	 * @param scriptWriter the scriptWriter to set
	 */
	public void setScriptWriterList(ArrayList<Person> scriptWriter) {
		this.scriptWriterList = scriptWriter;
	}
	
	public void addScriptWriter(Person newScriptWriter) {
		this.scriptWriterList.add(newScriptWriter);
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
	
	public void addCast(Person newCast) {
		this.castList.add(newCast);
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
	
	public void addProducer(Person producer) {
		this.producerList.add(producer);
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
	
	public void addComposer(Person newComposer) {
		this.composerList.add(newComposer);
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
	
	public void addEditor(Person editor) {
		this.editorList.add(editor);
	}

	/**
	 * @return the costumeDesigner
	 */
	public ArrayList<Person> getCostumeDesignerList() {
		return costumeDesignerList;
	}

	/**
	 * @param costumeDesigner the costumeDesigner to set
	 */
	public void setCostumeDesignerList(ArrayList<Person> costumeDesigner) {
		this.costumeDesignerList = costumeDesigner;
	}
	
	public void addCostumeDesigner(Person newCostumeDesigner) {
		this.costumeDesignerList.add(newCostumeDesigner);
	}

	/**
	 * @return the releaseYear
	 */
	public String getReleaseYear() {
		String result = "NULL";
		if (this.releaseYear != null) {
			result = "" + this.releaseYear;
		}
		return result;
	}
	
	/**
	 * @return the releaseYear
	 */
	public Integer getReleaseYearInt() {
		return this.releaseYear;
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
	public String getAward() {
		String result = "NULL";
		if (this.award != null) {
			result = "" + this.award;
		}
		return result;
	}
	
	/**
	 * @return the award
	 */
	public Integer getAwardInt() {
		return this.award;
	}

	/**
	 * @param award the award to set
	 */
	public void setAward(Integer award) {
		this.award = award;
	}
}
