package gui.functions;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import app.HL_xiewen4;
import database.DatabaseConnectionMovieApi;
import gui.pages.HomePagePanel;
import items.Movie;
import items.Person;

public class MoviePageInsertFunctions extends GeneralFunctions {

	private static final long serialVersionUID = -940593407764538703L;

	// Fields
	protected JTextArea nameOfMovie;
	protected ArrayList<ArrayList<JTextArea>> directorNameTable;
	protected ArrayList<ArrayList<JTextArea>> scriptWriterNameTable;
	protected ArrayList<ArrayList<JTextArea>> castNameTable;
	protected ArrayList<ArrayList<JTextArea>> producerNameTable;
	protected ArrayList<ArrayList<JTextArea>> composerNameTable;
	protected ArrayList<ArrayList<JTextArea>> editorNameTable;
	protected ArrayList<ArrayList<JTextArea>> costumeDesignerNameTable;
	protected JTextArea yearOfRelease;
	protected JButton addCastBtn;
	
	/**
	 * Instantiate movie panel
	 */
	protected void instantiateMoviePanel(String title, JButton submitButton) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 2;
		c.insets.bottom = 2;
		c.insets.left = 2;
		c.insets.right = 2;
		
		// Add Music message
		JLabel message = new JLabel(title);
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
		ArrayList<JTextArea> castRowFieldList = createNameRow("Cast", 14, 1);
		JTextArea award = createTextField("Cast Award:", 15, 8);
		castRowFieldList.add(award);
		castNameTable.add(castRowFieldList);
		
		// Add more cast button
		this.addCastBtn = new JButton("Add More Cast");
		addCastBtn.addActionListener(new ActionListener() {
			
			private int nextRowNum = 16;
			private int numOfCast = 1;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				numOfCast = numOfCast + 1;
				ArrayList<JTextArea> castRowFieldList = createNameRow("Cast", nextRowNum, numOfCast);
				JTextArea award = createTextField("Cast Award:", nextRowNum + 1, 8);
				castRowFieldList.add(award);
				castNameTable.add(castRowFieldList);
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
		this.yearOfRelease = this.createTextField("Year of Release:", 59, 0);
		
		// Submit
		JButton submitBtn = submitButton;
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
		this.createTextField("****** " + role + " #" + personNum + " ******", targetRowNum, 0, false);
		
		targetRowNum = targetRowNum + 1;
		JTextArea surname = this.createTextField(role + " Surname:", targetRowNum, 0);
		JTextArea firstName = this.createTextField(role + " First Name:", targetRowNum, 2);
		JTextArea middleName = this.createTextField(role + " Middle Name:", targetRowNum, 4);
		JTextArea gender = this.createTextField(role + " Gender:", targetRowNum, 6);
		nameList.addAll(Arrays.asList(surname, firstName, middleName, gender));
		
		return nameList;
	}

	
	/**
	 * Put all info provided by the user into a Movie object
	 * @return
	 */
	protected Movie getMovieInfo() {
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
		Integer targetYearOfRelease = textAreaToInt(this.yearOfRelease);
		
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
				
				// Gender
				Integer targetGender = textAreaToInt(person.get(3));
				targetPerson.setGender(targetGender);
				
				// Award
				if (person.size() == 5) {
					Integer targetAward = textAreaToInt(person.get(4));
					targetPerson.setAward(targetAward);
				}
				
				// Add person
				targetPersonList.add(targetPerson);
			}
		}
		
		return targetPersonList;
	}
}
