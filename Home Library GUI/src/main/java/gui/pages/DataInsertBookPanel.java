/**
 * 
 */
package gui.pages;

import java.awt.Dimension;
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
public class DataInsertBookPanel extends JPanel {

	private static final long serialVersionUID = 2059922932338622502L;
	
	// Fields
	private JTextArea bookName;
	private JTextArea bookISBN;
	private JTextArea publisherName;
	private JTextArea editionNumber;
	private ArrayList<ArrayList<JTextArea>> authorNameTable;
	private JTextArea numberOfPage;
	private JTextArea publicationYear;
	private ArrayList<JTextArea> tagList;
	private JTextArea description;

	public DataInsertBookPanel() {
		super();
		
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
		JButton addAuthorBtn = new JButton("Add More Author");
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
		JButton addMoreTagBtn = new JButton("Add More KeyWord");
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
	 * Create a submit button for this panel
	 * @param book the book object the button is going to use to add book info
	 * @return a submit JButton
	 */
	private JButton createSubmitButton() {
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Add all the info into the Book object
				Book book = getBookInfo();
				
				try {
					DatabaseConnectionBookApi.insertBook(book);
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
		String targetBookName;
		try {
			targetBookName = this.bookName.getText();
		} catch (NullPointerException e) {
			targetBookName = null;
		}
		
		// bookISBN
		String targetBookISBN;
		try {
			targetBookISBN = this.bookISBN.getText();
		} catch (NullPointerException e) {
			targetBookISBN = null;
		}
		
		// publisherName
		String targetPublisherName;
		try {
			targetPublisherName = this.publisherName.getText();
		} catch (NullPointerException e) {
			targetPublisherName = null;
		}
		
		// numberPage
		int targetNumOfPage;
		try {
			targetNumOfPage = Integer.parseInt(this.numberOfPage.getText());
		} catch (NullPointerException e) {
			targetNumOfPage = -1;
		}
		
		// publicationYear
		int targetPublicationYear;
		try {
			targetPublicationYear = Integer.parseInt(this.publicationYear.getText());
		} catch (NullPointerException e) {
			targetPublicationYear = -1;
		}
		
		
		Book book = new Book(targetBookISBN,
				targetBookName,
				targetPublisherName,
				targetNumOfPage,
				targetPublicationYear);
		
		// editionNumber
		int targetEditionNumber;
		try {
			targetEditionNumber = Integer.parseInt(this.editionNumber.getText());
		} catch (NullPointerException e) {
			targetEditionNumber = -1;
		}
		book.setEditionNumber(targetEditionNumber);
		
		// authorNameTable
		ArrayList<Person> targetAuthorList = new ArrayList<>();
		for (ArrayList<JTextArea> authorList : this.authorNameTable) {
			// author surname
			String targetAuthorSurname;
			try {
				targetAuthorSurname = authorList.get(0).getText();
			} catch (NullPointerException e) {
				targetAuthorSurname = null;
			}
			
			// author first name
			String targetAuthorFirstName;
			try {
				targetAuthorFirstName = authorList.get(1).getText();
			} catch (NullPointerException e) {
				targetAuthorFirstName = null;
			}
			
			// If both field is not null than add, else skip
			if ((targetAuthorSurname != null) && (targetAuthorFirstName != null)) {
				Person author = new Person(targetAuthorSurname, targetAuthorFirstName);
				
				// author middle name
				String targetAuthorMiddleName;
				try {
					targetAuthorMiddleName = authorList.get(2).getText();
				} catch (NullPointerException e) {
					targetAuthorMiddleName = null;
				}
				author.setMiddleName(targetAuthorMiddleName);
				
				// Append to targetAuthorList
				targetAuthorList.add(author);
			}
		}
		book.setAuthorList(targetAuthorList);
		
		// tagList
		ArrayList<String> targetTagList = new ArrayList<>();
		for (JTextArea tag : this.tagList) {
			String targetTag;
			try {
				targetTag = tag.getText();
			} catch (NullPointerException e) {
				targetTag = null;
			}
			
			if (targetTag != null) {
				targetTagList.add(targetTag);
			}
		}
		book.setKeyWords(targetTagList);
		
		// description
		String targetDescription;
		try {
			targetDescription = this.description.getText();
		} catch (NullPointerException e) {
			targetDescription = null;
		}
		book.setBookDescription(targetDescription);
		
		return book;
	}
}