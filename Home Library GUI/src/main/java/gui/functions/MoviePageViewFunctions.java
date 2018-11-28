package gui.functions;

import java.util.ArrayList;

import javax.swing.JTextArea;

import items.Movie;
import items.Person;

public class MoviePageViewFunctions extends MoviePageInsertFunctions {

	private static final long serialVersionUID = -4995639909227381535L;

	/**
	 * Set up the field environment for the oldMovie data
	 * @param musicAlbum
	 */
	protected void setUpFieldEnvirnment(Movie movie) {
		// Make sure there are enough cast fields
		for (int i = 1; i < movie.getCastList().size(); i++) {
			this.addCastBtn.doClick();
		}
	}
	
	/**
	 * Fill Movie info into the fields
	 * @param movie
	 */
	protected void fillMovieInfo(Movie movie) {
		// nameOfMovie
		this.nameOfMovie.setText(movie.getMovieName());
		
		// directorNameTable
		for (int i = 0; i < movie.getDirectorList().size(); i++) {
			ArrayList<JTextArea> directorNameList = this.directorNameTable.get(i);
			Person director = movie.getDirectorList().get(i);
			
			// surname
			directorNameList.get(0).setText(director.getSurname());
			
			// first name
			directorNameList.get(1).setText(director.getFirstName());
			
			// middle name
			directorNameList.get(2).setText(director.getMiddleName());
			
			// gender
			directorNameList.get(3).setText(formatIntToStr(director.getGender()));
		}
		
		// scriptWriterNameTable
		for (int i = 0; i < movie.getScriptWriterList().size(); i++) {
			ArrayList<JTextArea> scriptWriterNameList = this.scriptWriterNameTable.get(i);
			Person scriptWriter = movie.getScriptWriterList().get(i);
			
			// surname
			scriptWriterNameList.get(0).setText(scriptWriter.getSurname());
			
			// first name
			scriptWriterNameList.get(1).setText(scriptWriter.getFirstName());
			
			// middle name
			scriptWriterNameList.get(2).setText(scriptWriter.getMiddleName());
			
			// gender
			scriptWriterNameList.get(3).setText(formatIntToStr(scriptWriter.getGender()));
		}
		
		// castNameTable
		for (int i = 0; i < movie.getCastList().size(); i++) {
			ArrayList<JTextArea> castNameList = this.castNameTable.get(i);
			Person cast = movie.getCastList().get(i);
			
			// surname
			castNameList.get(0).setText(cast.getSurname());
			
			// first name
			castNameList.get(1).setText(cast.getFirstName());
			
			// middle name
			castNameList.get(2).setText(cast.getMiddleName());
			
			// gender
			castNameList.get(3).setText(formatIntToStr(cast.getGender()));
			
			// award
			castNameList.get(4).setText(formatIntToStr(cast.getAward()));
		}
		
		// producerNameTable
		for (int i = 0; i < movie.getProducerList().size(); i++) {
			ArrayList<JTextArea> producerNameList = this.producerNameTable.get(i);
			Person producer = movie.getProducerList().get(i);
			
			// surname
			producerNameList.get(0).setText(producer.getSurname());
			
			// first name
			producerNameList.get(1).setText(producer.getFirstName());
			
			// middle name
			producerNameList.get(2).setText(producer.getMiddleName());
			
			// gender
			producerNameList.get(3).setText(formatIntToStr(producer.getGender()));
		}
		
		// composerNameTable
		for (int i = 0; i < movie.getComposerList().size(); i++) {
			ArrayList<JTextArea> composerNameList = this.composerNameTable.get(i);
			Person composer = movie.getComposerList().get(i);
			
			// surname
			composerNameList.get(0).setText(composer.getSurname());
			
			// first name
			composerNameList.get(1).setText(composer.getFirstName());
			
			// middle name
			composerNameList.get(2).setText(composer.getMiddleName());
			
			// middle name
			composerNameList.get(3).setText(formatIntToStr(composer.getGender()));
		}
		
		// editorNameTable
		for (int i = 0; i < movie.getEditorList().size(); i++) {
			ArrayList<JTextArea> editorNameList = this.editorNameTable.get(i);
			Person editor = movie.getEditorList().get(i);
			
			// surname
			editorNameList.get(0).setText(editor.getSurname());
			
			// first name
			editorNameList.get(1).setText(editor.getFirstName());
			
			// middle name
			editorNameList.get(2).setText(editor.getMiddleName());
			
			// gender
			editorNameList.get(3).setText(formatIntToStr(editor.getGender()));
		}
		
		// costumeDesignerNameTable
		for (int i = 0; i < movie.getCostumeDesignerList().size(); i++) {
			ArrayList<JTextArea> costumeDesignerNameList = this.costumeDesignerNameTable.get(i);
			Person costumeDesigner = movie.getCostumeDesignerList().get(i);
			
			// surname
			costumeDesignerNameList.get(0).setText(costumeDesigner.getSurname());
			
			// first name
			costumeDesignerNameList.get(1).setText(costumeDesigner.getFirstName());
			
			// middle name
			costumeDesignerNameList.get(2).setText(costumeDesigner.getMiddleName());
			
			// gender
			costumeDesignerNameList.get(3).setText(formatIntToStr(costumeDesigner.getGender()));
		}
		
		// yearOfRelease
		this.yearOfRelease.setText(formatIntToStr(movie.getReleaseYear()));
	}
	
}
