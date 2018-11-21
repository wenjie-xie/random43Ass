/**
 * 
 */
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
import database.DatabaseConnectionBookApi;
import items.Book;
import items.Person;

/**
 * @author xiewen4
 *
 */
public class DataUpdateBookEditPanel extends JPanel {

	private static final long serialVersionUID = 6514547048807147368L;
	
	// Fields
	private JTextArea bookName;
	private JTextArea bookISBN;
	private JTextArea publisherName;
	private JTextArea editionNumber;
	private ArrayList<ArrayList<JTextArea>> authorNameTable;
	private JButton addAuthorBtn;
	private JTextArea numberOfPage;
	private JTextArea publicationYear;
	private ArrayList<JTextArea> tagList;
	private JButton addMoreTagBtn;
	private JTextArea description;
	private Book oldBookInfo;

	public DataUpdateBookEditPanel(Book book) {
		super();
		this.oldBookInfo = book;
		
		// Create all the fields for a book
		displayBookFields();
		
		// instantiate the field so there will be enough fields for tags and author
		setUpFieldEnvirnment(book);
		
		// Fill field with info
		fillBookInfo(book);
		
	}
	
	/**
	 * Create all the fields in Book
	 */
	private void displayBookFields() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets.top = 2;
		c.insets.bottom = 2;
		c.insets.left = 2;
		c.insets.right = 2;
		
		// Add Book message
		JLabel message = new JLabel("Add New Book:");
		message.setFont(new Font(message.getName(), Font.PLAIN, 30));
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(message, c);
		
		// Book Name
		this.bookName = this.createTextField("Book Name:", 1, 0);
		
		// Book ISBN
		this.bookISBN = this.createTextField("Book ISBN:", 2, 0);
		
		// Publisher Name
		this.publisherName = this.createTextField("Publisher Name:", 3, 0);
		
		// Edition Number
		this.editionNumber = this.createTextField("Edition Number:", 4, 0);
		
		// List Of Authors (Surname, First, Middle) (1-5)
		this.authorNameTable = new ArrayList<>();
		authorNameTable.add(this.addAnotherAuthor(5, 1));
		int nextRowNum = 7;
		
		// Add more author button
		addAuthorBtn = new JButton("Add More Author");
		addAuthorBtn.addActionListener(new ActionListener() {
			
			private int startRowNum = nextRowNum;
			private int numOfAuthor = 1;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				numOfAuthor = numOfAuthor + 1;
				authorNameTable.add(addAnotherAuthor(startRowNum, numOfAuthor));
				this.startRowNum = this.startRowNum + authorNameTable.get(0).size();
				
				// disable this button if the number of author is reached
				if (numOfAuthor == 5) {
					addAuthorBtn.setEnabled(false);
				}
				
				revalidate();
				repaint();
			}
		});
		// Pretend there are 4 more author fields row = 18, each author field takes 2 rows
	
		c.gridx = 0;
		c.gridy = 18;
		this.add(addAuthorBtn, c);
		
		// Number of Page
		this.numberOfPage = this.createTextField("Number of Page:", 19, 0);
		
		// Publication Year
		this.publicationYear = this.createTextField("Publication Year:", 20, 0);
		
		// Tags (Key words) (1-20)
		this.tagList = new ArrayList<>();
		
		JTextArea tag = this.createTextField("Key Word 1:", 21, 0);
		tagList.add(tag);
		
		// Add more tag button
		addMoreTagBtn = new JButton("Add More KeyWord");
		addMoreTagBtn.addActionListener(new ActionListener() {
			
			private int numberOfTags = 0;
			private int colNum = 2;
			private int rowNum = 0;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				numberOfTags += 1;
				JTextArea tag = createTextField("Key Word " + (numberOfTags + 1) + ":", 21 + rowNum, colNum);
				tagList.add(tag);
				
				colNum += 2;
				// If colNum is bigger than 5 reset to 0
				if (colNum > 5) {
					colNum = 0;
					rowNum += 1;
				}
				
				// There can only be 20 tags
				if (numberOfTags == 19) {
					addMoreTagBtn.setEnabled(false);
				}
				
				revalidate();
				repaint();
			}
		});
		// Pretend there are 19/3 = 7 more rows
		
		c.gridx = 0;
		c.gridy = 28;
		this.add(addMoreTagBtn, c);
		
		
		// Description of the Book
		JLabel name = new JLabel("Book Description:");
		c.gridx = 0;
		c.gridy = 29;
		this.add(name, c);
		this.description = new JTextArea();
		c.gridy = 30;
		c.gridwidth = 6;
		this.add(description, c);
		
		// Submit
		JButton submitBtn = this.createSubmitButton();
		c.gridwidth = 1;
		c.gridy = 31;
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
		}
		
		return null;
	}
	
	private JTextArea createTextField(String fieldName, int rowNum, int colNum) {
		return createTextField(fieldName, rowNum, colNum, true);
	}
	
	/**
	 * The purpose of this method is to add another author
	 * @param startRow
	 * @param authorNumber
	 * @return [authorSurname, authorFirstName, authroMiddleName]
	 */
	private ArrayList<JTextArea> addAnotherAuthor(int startRow, int authorNumber) {
		ArrayList<JTextArea> authorNameList = new ArrayList<>();
		int targetRow = startRow;
		
		this.createTextField("****** Author #" + authorNumber + " ******", targetRow, 0, false);
		
		targetRow = targetRow + 1;
		JTextArea authorSurname = this.createTextField("Author Surname:", targetRow, 0);
		JTextArea authorFirstName = this.createTextField("Author First Name:", targetRow, 2);
		JTextArea authorMiddleName = this.createTextField("Author Middle Name:", targetRow, 4);
		authorNameList.addAll(Arrays.asList(authorSurname, authorFirstName, authorMiddleName));
		
		return authorNameList;
	}
	
	/**
	 * Set up the fields to fill in info
	 * @param book the book info that needs setup
	 */
	private void setUpFieldEnvirnment(Book book) {
		// Add more author field
		for (int authorNum = 1; authorNum < book.getAuthorList().size(); authorNum++) {
			addAuthorBtn.doClick();
		}
		
		// Add more tag field
		for (int tagNum = 1; tagNum < book.getKeyWords().size(); tagNum++) {
			addMoreTagBtn.doClick();
		}
	}
	
	/**
	 * Fill the information into the fields
	 * @param book the object containing all the book info
	 */
	private void fillBookInfo(Book book) {
		// bookName
		bookName.setText(this.formatString(book.getBookName()));
		
		// bookISBN
		bookISBN.setText(this.formatString(book.getBookISBN()));
		
		// publisherName
		publisherName.setText(this.formatString(book.getPublisherName()));
		
		// editionNumber
		editionNumber.setText(this.formatString(book.getEditionNumber()));
		
		// authorNameTable
		int authorIndex = 0;
		for (ArrayList<JTextArea> authorFields : authorNameTable) {
			Person currAuthor = book.getAuthorList().get(authorIndex);
			
			// Surname
			authorFields.get(0).setText(this.formatString(currAuthor.getSurname()));
			
			// FirstName
			authorFields.get(1).setText(this.formatString(currAuthor.getFirstName()));
			
			// MiddleName
			authorFields.get(2).setText(this.formatString(currAuthor.getMiddleName()));
			
			authorIndex = authorIndex + 1;
		}
		
		// numberOfPage
		numberOfPage.setText(this.formatString(book.getNumOfPage()));
		
		// publicationYear
		publicationYear.setText(this.formatString(book.getPublicationYear()));
		
		// tagList
		int tagIndex = 0;
		for (JTextArea tagField : tagList) {
			String currTag = book.getKeyWords().get(tagIndex).replaceAll("'", "");
			tagField.setText(this.formatString(currTag));
		}
		
		// description
		description.setText(this.formatString(book.getBookDescription()));
	}
	
	private String formatString(String str) {
		return str.replaceAll("'", "").replaceAll("NULL", "").replaceAll("-1", "");
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
				// Add all the info into the Book object
				Book newBook = getBookInfo();
				
				try {
					DatabaseConnectionBookApi.compareAndUpdateBookInfo(oldBookInfo, newBook);
					HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		return submitBtn;
	}
	
	/**
	 * The purpose of this function is to gather all the book info from the fields
	 * and add them to the book object
	 * @return a book object containing all the info entered by the user
	 */
	private Book getBookInfo() {
		// bookName
		String targetBookName = textAreaToString(this.bookName);
		
		// bookISBN
		String targetBookISBN = textAreaToString(this.bookISBN);
		
		// publisherName
		String targetPublisherName = textAreaToString(this.publisherName);
		
		// numberPage
		int targetNumOfPage = textAreaToInt(this.numberOfPage);
		
		// publicationYear
		int targetPublicationYear = textAreaToInt(this.publicationYear);
		
		
		Book book = new Book(targetBookISBN,
				targetBookName,
				targetPublisherName,
				targetNumOfPage,
				targetPublicationYear);
		
		// editionNumber
		int targetEditionNumber = textAreaToInt(this.editionNumber);
		book.setEditionNumber(targetEditionNumber);
		
		// authorNameTable
		ArrayList<Person> targetAuthorList = new ArrayList<>();
		for (ArrayList<JTextArea> authorList : this.authorNameTable) {
			// author surname
			String targetAuthorSurname = textAreaToString(authorList.get(0));
			
			// author first name
			String targetAuthorFirstName = textAreaToString(authorList.get(1));
			
			// If both field is not null than add, else skip
			if ((targetAuthorSurname != null) && (targetAuthorFirstName != null)) {
				Person author = new Person(targetAuthorSurname, targetAuthorFirstName);
				
				// author middle name
				String targetAuthorMiddleName = textAreaToString(authorList.get(2));
				author.setMiddleName(targetAuthorMiddleName);
				
				// Append to targetAuthorList
				targetAuthorList.add(author);
			}
		}
		book.setAuthorList(targetAuthorList);
		
		// tagList
		ArrayList<String> targetTagList = new ArrayList<>();
		for (JTextArea tag : this.tagList) {
			String targetTag = textAreaToString(tag);
			
			if (targetTag != null) {
				targetTagList.add(targetTag);
			}
		}
		book.setKeyWords(targetTagList);
		
		// description
		String targetDescription = textAreaToString(this.description);
		book.setBookDescription(targetDescription);
		
		return book;
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
}
