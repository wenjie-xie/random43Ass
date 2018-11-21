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
		this.oldMovie = movie;
		
		// initialize fields
		super.instantiateMoviePanel("Update Movie:", this.createSubmitButton());
		
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
				
				try {
					DatabaseConnectionMovieApi.compareAndUpdateMovie(oldMovie, newMovie);
					HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
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
		}
		
		// yearOfRelease
		this.yearOfRelease.setText(formatString(movie.getReleaseYear()));
	}
	
	
	private String formatString(String str) {
		return str.replaceAll("'", "").replaceAll("NULL", "").replaceAll("-1", "");
	}
}
