package gui.pages;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.HL_xiewen4;
import database.DatabaseConnectionMovieApi;
import database.DatabaseConnectionMusicAlbumApi;
import gui.functions.MoviePageInsertFunctions;
import items.Movie;
import items.MusicAlbum;
import items.Person;

/**
 * This is the panel for Data > Insert > Movie
 * @author xiewen4
 *
 */
public class DataInsertMoviePanel extends MoviePageInsertFunctions {

	private static final long serialVersionUID = -618204154220577240L;
	
	public DataInsertMoviePanel() {
		super();
		instantiateMoviePanel("Add New Movies:", this.createSubmitButton());
		
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
				Movie movie = getMovieInfo();

				DatabaseConnectionMovieApi.insertMovie(movie);
				HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
				
			}
		});
		
		return submitBtn;
	}
}
