package gui.pages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTextArea;

import app.HL_xiewen4;
import database.DatabaseConnectionMovieApi;
import items.Movie;
import items.Person;

public class DataUpdateMovieEditPanel extends DataInsertMoviePanel {

	private static final long serialVersionUID = -618204154220577240L;
	
	
	private Movie oldMovie;
	
	public DataUpdateMovieEditPanel(Movie movie) {
		super("Update Movie:");
		this.oldMovie = movie;
		
		// setup fields
		setUpFieldEnvirnment(movie);
		
		// fill movie info
		fillMovieInfo(movie);
	}
	
	
	/**
	 * Create a submit button for this panel
	 * @return a submit JButton
	 */
	private JButton createSubmitButton() {
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Add all the info into the Music Album object
				Movie newMovie = getMovieInfo();
				
				DatabaseConnectionMovieApi.compareAndUpdateMovie(oldMovie, newMovie);
				HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
				
			}
		});
		
		return submitBtn;
	}


	/**
	 * Set up the field environment for the oldMovie data
	 * @param musicAlbum
	 */
	private void setUpFieldEnvirnment(Movie movie) {
		// Make sure there are enough cast fields
		for (int i = 1; i < movie.getCastList().size(); i++) {
			this.addCastBtn.doClick();
		}
	}
	
	/**
	 * Fill Movie info into the fields
	 * @param movie
	 */
	private void fillMovieInfo(Movie movie) {
		// nameOfMovie
		this.nameOfMovie.setText(formatString(movie.getMovieName()));
		
		// directorNameTable
		for (int i = 0; i < movie.getDirectorList().size(); i++) {
			ArrayList<JTextArea> directorNameList = this.directorNameTable.get(i);
			Person director = movie.getDirectorList().get(i);
			
			// surname
			directorNameList.get(0).setText(formatString(director.getSurname()));
			
			// first name
			directorNameList.get(1).setText(formatString(director.getFirstName()));
			
			// middle name
			directorNameList.get(2).setText(formatString(director.getMiddleName()));
			
			// gender
			directorNameList.get(3).setText(formatString(director.getGender()));
		}
		
		// scriptWriterNameTable
		for (int i = 0; i < movie.getScriptWriterList().size(); i++) {
			ArrayList<JTextArea> scriptWriterNameList = this.scriptWriterNameTable.get(i);
			Person scriptWriter = movie.getScriptWriterList().get(i);
			
			// surname
			scriptWriterNameList.get(0).setText(formatString(scriptWriter.getSurname()));
			
			// first name
			scriptWriterNameList.get(1).setText(formatString(scriptWriter.getFirstName()));
			
			// middle name
			scriptWriterNameList.get(2).setText(formatString(scriptWriter.getMiddleName()));
			
			// gender
			scriptWriterNameList.get(3).setText(formatString(scriptWriter.getGender()));
		}
		
		// castNameTable
		for (int i = 0; i < movie.getCastList().size(); i++) {
			ArrayList<JTextArea> castNameList = this.castNameTable.get(i);
			Person cast = movie.getCastList().get(i);
			
			// surname
			castNameList.get(0).setText(formatString(cast.getSurname()));
			
			// first name
			castNameList.get(1).setText(formatString(cast.getFirstName()));
			
			// middle name
			castNameList.get(2).setText(formatString(cast.getMiddleName()));
			
			// gender
			castNameList.get(3).setText(formatString(cast.getGender()));
		}
		
		// producerNameTable
		for (int i = 0; i < movie.getProducerList().size(); i++) {
			ArrayList<JTextArea> producerNameList = this.producerNameTable.get(i);
			Person producer = movie.getProducerList().get(i);
			
			// surname
			producerNameList.get(0).setText(formatString(producer.getSurname()));
			
			// first name
			producerNameList.get(1).setText(formatString(producer.getFirstName()));
			
			// middle name
			producerNameList.get(2).setText(formatString(producer.getMiddleName()));
			
			// gender
			producerNameList.get(3).setText(formatString(producer.getGender()));
		}
		
		// composerNameTable
		for (int i = 0; i < movie.getComposerList().size(); i++) {
			ArrayList<JTextArea> composerNameList = this.composerNameTable.get(i);
			Person composer = movie.getComposerList().get(i);
			
			// surname
			composerNameList.get(0).setText(formatString(composer.getSurname()));
			
			// first name
			composerNameList.get(1).setText(formatString(composer.getFirstName()));
			
			// middle name
			composerNameList.get(2).setText(formatString(composer.getMiddleName()));
			
			// middle name
			composerNameList.get(3).setText(formatString(composer.getGender()));
		}
		
		// editorNameTable
		for (int i = 0; i < movie.getEditorList().size(); i++) {
			ArrayList<JTextArea> editorNameList = this.editorNameTable.get(i);
			Person editor = movie.getEditorList().get(i);
			
			// surname
			editorNameList.get(0).setText(formatString(editor.getSurname()));
			
			// first name
			editorNameList.get(1).setText(formatString(editor.getFirstName()));
			
			// middle name
			editorNameList.get(2).setText(formatString(editor.getMiddleName()));
			
			// gender
			editorNameList.get(3).setText(formatString(editor.getGender()));
		}
		
		// costumeDesignerNameTable
		for (int i = 0; i < movie.getCostumeDesignerList().size(); i++) {
			ArrayList<JTextArea> costumeDesignerNameList = this.costumeDesignerNameTable.get(i);
			Person costumeDesigner = movie.getCostumeDesignerList().get(i);
			
			// surname
			costumeDesignerNameList.get(0).setText(formatString(costumeDesigner.getSurname()));
			
			// first name
			costumeDesignerNameList.get(1).setText(formatString(costumeDesigner.getFirstName()));
			
			// middle name
			costumeDesignerNameList.get(2).setText(formatString(costumeDesigner.getMiddleName()));
			
			// gender
			costumeDesignerNameList.get(3).setText(formatString(costumeDesigner.getGender()));
		}
		
		// yearOfRelease
		this.yearOfRelease.setText(formatString(movie.getReleaseYear()));
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
