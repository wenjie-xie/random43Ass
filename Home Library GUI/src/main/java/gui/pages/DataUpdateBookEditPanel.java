/**
 * 
 */
package gui.pages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTextArea;

import app.HL_xiewen4;
import database.DatabaseConnectionBookApi;
import items.Book;
import items.Person;

/**
 * @author xiewen4
 *
 */
public class DataUpdateBookEditPanel extends DataInsertBookPanel {

	private static final long serialVersionUID = 6514547048807147368L;
	
	private Book oldBookInfo;

	public DataUpdateBookEditPanel(Book book) {
		super("Update Book:");
		
		this.oldBookInfo = book;
		
		// instantiate the field so there will be enough fields for tags and author
		setUpFieldEnvirnment(book);
		
		// Fill field with info
		fillBookInfo(book);
		
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
			
			// Gender
			authorFields.get(3).setText(this.formatString(currAuthor.getGender()));
			
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
	
}
