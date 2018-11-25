package gui.functions;

import java.util.ArrayList;

import javax.swing.JTextArea;

import items.Book;
import items.Person;

public class BookPageViewFunctions extends BookPageInsertFunctions {

	private static final long serialVersionUID = -4021800127730337310L;
	
	/**
	 * Set up the fields to fill in info
	 * @param book the book info that needs setup
	 */
	protected void setUpFieldEnvirnment(Book book) {
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
	protected void fillBookInfo(Book book) {
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
			String currTag = this.formatString(book.getKeyWords().get(tagIndex));
			tagField.setText(this.formatString(currTag));
			tagIndex = tagIndex + 1;
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

}
