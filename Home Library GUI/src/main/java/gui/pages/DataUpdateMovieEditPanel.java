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
import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.HL_xiewen4;
import database.DatabaseConnectionMovieApi;
import items.Movie;
import items.MusicAlbum;
import items.Person;

public class DataUpdateMovieEditPanel extends JPanel {

	private static final long serialVersionUID = -618204154220577240L;
	
	// Fields
	private JTextArea nameOfMovie;
	private ArrayList<ArrayList<JTextArea>> directorNameTable;
	private ArrayList<ArrayList<JTextArea>> scriptWriterNameTable;
	private ArrayList<ArrayList<JTextArea>> castNameTable;
	private ArrayList<ArrayList<JTextArea>> producerNameTable;
	private ArrayList<ArrayList<JTextArea>> composerNameTable;
	private ArrayList<ArrayList<JTextArea>> editorNameTable;
	private ArrayList<ArrayList<JTextArea>> costumeDesignerNameTable;
	private JTextArea yearOfRelease;
	private JButton addCastBtn;
	
	private Movie oldMovie;
	
	public DataUpdateMovieEditPanel(Movie movie) {
		super();
		this.oldMovie = movie;
		
		// initialize fields
		initalizeFields();
		
		// setup fields
		setUpFieldEnvirnment(movie);
		
		// fill movie info
		fillMovieInfo(movie);
	}
	
	private void initalizeFields() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 2;
		c.insets.bottom = 2;
		c.insets.left = 2;
		c.insets.right = 2;
		
		// Add Music message
		JLabel message = new JLabel("Add New Movie:");
		message.setFont(new Font(message.getName(), Font.PLAIN, 30));
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(message, c);
		
		// Name of the Movie
		nameOfMovie = this.createTextField("Name of the Movie:", 1, 0);
		
		// Director
		directorNameTable = new ArrayList<ArrayList<JTextArea>>();
		directorNameTable.add(this.createNameRow("Director", 2, 1));
		
		directorNameTable.add(this.createNameRow("Director", 4, 2));
		
		directorNameTable.add(this.createNameRow("Director", 6, 3));
		
		// Script Writer (1-3)
		scriptWriterNameTable = new ArrayList<ArrayList<JTextArea>>();
		scriptWriterNameTable.add(this.createNameRow("Script Writer", 8, 1));
		
		scriptWriterNameTable.add(this.createNameRow("Script Writer", 10, 2));
		
		scriptWriterNameTable.add(this.createNameRow("Script Writer", 12, 3));
		
		// Cast (1-10)
		castNameTable = new ArrayList<>();
		castNameTable.add(this.createNameRow("Cast", 14, 1));
		
		// Add more cast button
		addCastBtn = new JButton("Add More Cast");
		addCastBtn.addActionListener(new ActionListener() {
			
			private int nextRowNum = 16;
			private int numOfCast = 1;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				numOfCast = numOfCast + 1;
				castNameTable.add(createNameRow("Cast", nextRowNum, numOfCast));
				nextRowNum = nextRowNum + 2;
				
				// disable this button if the number of author is reached
				if (numOfCast == 10) {
					addCastBtn.setEnabled(false);
				}
				
				revalidate();
				repaint();
			}
		});
		// Pretend there are 9 more cast fields row = 34, each field takes up 2 rows
	
		c.gridx = 0;
		c.gridy = 34;
		this.add(addCastBtn, c);
		
		// Producers (1-3)
		producerNameTable = new ArrayList<ArrayList<JTextArea>>();
		producerNameTable.add(this.createNameRow("Producer", 35, 1));
		
		producerNameTable.add(this.createNameRow("Producer", 37, 2));
		
		producerNameTable.add(this.createNameRow("Producer", 39, 3));
		
		// Composers (1-3)
		composerNameTable = new ArrayList<ArrayList<JTextArea>>();
		composerNameTable.add(this.createNameRow("Composer", 41, 1));
		
		composerNameTable.add(this.createNameRow("Composer", 43, 2));
		
		composerNameTable.add(this.createNameRow("Composer", 45, 3));
		
		// Editor (1-3)
		editorNameTable = new ArrayList<ArrayList<JTextArea>>();
		editorNameTable.add(this.createNameRow("Editor", 47, 1));
		
		editorNameTable.add(this.createNameRow("Editor", 49, 2));
		
		editorNameTable.add(this.createNameRow("Editor", 51, 3));
		
		// Costume Designer (1-3)
		costumeDesignerNameTable = new ArrayList<ArrayList<JTextArea>>();
		costumeDesignerNameTable.add(this.createNameRow("Costume Designer", 53, 1));
		
		costumeDesignerNameTable.add(this.createNameRow("Costume Designer", 55, 2));
		
		costumeDesignerNameTable.add(this.createNameRow("Costume Designer", 57, 3));
		
		// Year of Release
		JTextArea yearOfRelease = this.createTextField("Year of Release:", 59, 0);
		
		// Submit
		JButton submitBtn = this.createSubmitButton();
		c.gridwidth = 1;
		c.gridy = 60;
		c.gridx = 0;
		c.weightx = 0.5;
		this.add(submitBtn, c);
		
		// Cancel
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
				
			}
		});
		c.gridx = 1;
		this.add(cancelBtn, c);
	}
	
	
	/**
	 * The purpose of this method is to create a text field for each given fieldName
	 * @param fieldName
	 * @param rowNum
	 * @param colNum
	 * @return
	 */
	private JTextArea createTextField(String fieldName, int rowNum, int colNum, boolean showTextArea) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 2;
		c.insets.bottom = 2;
		c.insets.left = 2;
		c.insets.right = 2;
		
		JLabel name = new JLabel(fieldName);
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = colNum;
		c.gridy = rowNum;
		this.add(name, c);
		
		if (showTextArea) {
			JTextArea field = new JTextArea();
			c.weightx = 1;
			c.gridx = colNum + 1;
			this.add(field, c);
			
			return field;
		} else {
			return null;
		}
	}
	
	private JTextArea createTextField(String fieldName, int rowNum, int colNum) {
		return createTextField(fieldName, rowNum, colNum, true);
	}
	
	
	/**
	 * The purpose of this method is to return a list of Name of a given role
	 * @param role
	 * @param rowNum
	 * @param personNum
	 * @return [surname, firstname, middlename]
	 */
	private ArrayList<JTextArea> createNameRow(String role, int rowNum, int personNum) {
		ArrayList<JTextArea> nameList = new ArrayList<>();
		int targetRowNum = rowNum;
		this.createTextField("****** " + role + " #" + personNum + " ******", targetRowNum, 0);
		
		targetRowNum = targetRowNum + 1;
		JTextArea surname = this.createTextField(role + " Surname:", targetRowNum, 0);
		JTextArea firstName = this.createTextField(role + " First Name:", targetRowNum, 2);
		JTextArea middleName = this.createTextField(role + " Middle Name:", targetRowNum, 4);
		nameList.addAll(Arrays.asList(surname, firstName, middleName));
		
		return nameList;
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
	 * Put all info provided by the user into a Movie object
	 * @return
	 */
	private Movie getMovieInfo() {
		// nameOfMovie
		String targetMovieName = textAreaToString(this.nameOfMovie);
		
		// directorNameTable
		ArrayList<Person> targetDirectorList = nameTableToPersonList(directorNameTable);
		
		// scriptWriterNameTable
		ArrayList<Person> targetScriptWriterList = nameTableToPersonList(scriptWriterNameTable);
		
		// castNameTable
		ArrayList<Person> targetCastList = nameTableToPersonList(castNameTable);
		
		// producerNameTable
		ArrayList<Person> targetProducerList = nameTableToPersonList(producerNameTable);
		
		// composerNameTable
		ArrayList<Person> targetComposerList = nameTableToPersonList(composerNameTable);
		
		// editorNameTable
		ArrayList<Person> targetEditorList = nameTableToPersonList(editorNameTable);
		
		// costumeDesignerNameTable
		ArrayList<Person> targetCostumeDesignerList = nameTableToPersonList(costumeDesignerNameTable);
		
		// yearOfRelease
		int targetYearOfRelease = textAreaToInt(this.yearOfRelease);
		
		Movie movie = new Movie(targetMovieName, targetYearOfRelease);
		movie.setDirectorList(targetDirectorList);
		movie.setScriptWriterList(targetScriptWriterList);
		movie.setCastList(targetCastList);
		movie.setProducerList(targetProducerList);
		movie.setComposerList(targetComposerList);
		movie.setEditorList(targetEditorList);
		movie.setCostumeDesignerList(targetCostumeDesignerList);

		
		return movie;
	}
	
	private ArrayList<Person> nameTableToPersonList(ArrayList<ArrayList<JTextArea>> nameTable) {
		ArrayList<Person> targetPersonList = new ArrayList<>();
		for (ArrayList<JTextArea> person : nameTable) {
			// Surname
			String targetPersonSurname = textAreaToString(person.get(0));
			
			// First Name
			String targetPersonFirstName = textAreaToString(person.get(1));
			
			if ((targetPersonSurname != null) && (targetPersonFirstName != null)) {
				// Create person
				Person targetPerson = new Person(targetPersonSurname, targetPersonFirstName);
				// Middle Name
				String targetPersonMiddleName = textAreaToString(person.get(2));
				targetPerson.setMiddleName(targetPersonMiddleName);
				
				// Add person
				targetPersonList.add(targetPerson);
			}
		}
		
		return targetPersonList;
	}
	
	/**
	 * Get data from the text area as int
	 * @param textArea
	 * @return a int given, otherwise return -1
	 */
	private int textAreaToInt(JTextArea textArea) {
		int target;
		try {
			target = Integer.parseInt(textArea.getText());
		} catch (Exception e) {
			target = -1;
		}
		return target;
	}
	
	/**
	 * Get data from the text area as String
	 * @param textArea
	 * @return a String given, otherwise return null
	 */
	private String textAreaToString(JTextArea textArea) {
		String target;
		try {
			target = textArea.getText();
		} catch (NullPointerException e) {
			target = null;
		}
		return target;
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
