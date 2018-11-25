package gui.pages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTextArea;

import app.HL_xiewen4;
import database.DatabaseConnectionMovieApi;
import gui.functions.MoviePageViewFunctions;
import items.Movie;
import items.Person;

public class DataUpdateMovieEditPanel extends MoviePageViewFunctions {

	private static final long serialVersionUID = -618204154220577240L;
	
	
	private Movie oldMovie;
	
	public DataUpdateMovieEditPanel(Movie movie) {
		this.oldMovie = movie;
		
		// initialize Movie fields
		instantiateMoviePanel("Update Movie:", this.createSubmitButton());
		
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
}
