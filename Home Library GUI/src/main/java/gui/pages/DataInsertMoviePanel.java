package gui.pages;

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

/**
 * This is the panel for Data > Insert > Movie
 * @author xiewen4
 *
 */
public class DataInsertMoviePanel extends JPanel {

	private static final long serialVersionUID = -618204154220577240L;
	
	public DataInsertMoviePanel() {
		super();
		
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
		JTextArea nameIfMovie = this.createTextField("Name of the Movie:", 1, 0);
		
		// Director
		ArrayList<ArrayList<JTextArea>> directorNameTable = new ArrayList<ArrayList<JTextArea>>();
		directorNameTable.add(this.createNameRow("Director", 2, 1));
		
		directorNameTable.add(this.createNameRow("Director", 4, 2));
		
		directorNameTable.add(this.createNameRow("Director", 6, 3));
		
		// Script Writer (1-3)
		ArrayList<ArrayList<JTextArea>> scriptWriterNameTable = new ArrayList<ArrayList<JTextArea>>();
		scriptWriterNameTable.add(this.createNameRow("Script Writer", 8, 1));
		
		scriptWriterNameTable.add(this.createNameRow("Script Writer", 10, 2));
		
		scriptWriterNameTable.add(this.createNameRow("Script Writer", 12, 3));
		
		// Cast (1-10)
		ArrayList<ArrayList<JTextArea>> castNameTable = new ArrayList<>();
		castNameTable.add(this.createNameRow("Cast", 14, 1));
		
		// Add more cast button
		JButton addCastBtn = new JButton("Add More Cast");
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
		ArrayList<ArrayList<JTextArea>> producerNameTable = new ArrayList<ArrayList<JTextArea>>();
		producerNameTable.add(this.createNameRow("Producer", 35, 1));
		
		producerNameTable.add(this.createNameRow("Producer", 37, 2));
		
		producerNameTable.add(this.createNameRow("Producer", 39, 3));
		
		// Composers (1-3)
		ArrayList<ArrayList<JTextArea>> composerNameTable = new ArrayList<ArrayList<JTextArea>>();
		composerNameTable.add(this.createNameRow("Composer", 41, 1));
		
		composerNameTable.add(this.createNameRow("Composer", 43, 2));
		
		composerNameTable.add(this.createNameRow("Composer", 45, 3));
		
		// Editor (1-3)
		ArrayList<ArrayList<JTextArea>> editorNameTable = new ArrayList<ArrayList<JTextArea>>();
		editorNameTable.add(this.createNameRow("Editor", 47, 1));
		
		editorNameTable.add(this.createNameRow("Editor", 49, 2));
		
		editorNameTable.add(this.createNameRow("Editor", 51, 3));
		
		// Costume Designer (1-3)
		ArrayList<ArrayList<JTextArea>> costumeDesignerNameTable = new ArrayList<ArrayList<JTextArea>>();
		costumeDesignerNameTable.add(this.createNameRow("Costume Designer", 53, 1));
		
		costumeDesignerNameTable.add(this.createNameRow("Costume Designer", 55, 2));
		
		costumeDesignerNameTable.add(this.createNameRow("Costume Designer", 57, 3));
		
		// Year of Release
		JTextArea yearOfRelease = this.createTextField("Year of Release:", 59, 0);
		
		// Submit
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
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
				// TODO Auto-generated method stub
				
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
}
