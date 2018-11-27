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
import gui.functions.BookPageViewFunctions;
import items.Book;
import items.Person;

/**
 * @author xiewen4
 *
 */
public class DataUpdateBookEditPanel extends BookPageViewFunctions {

	private static final long serialVersionUID = 6514547048807147368L;
	
	private Book oldBookInfo;

	public DataUpdateBookEditPanel(Book book) {
		this.oldBookInfo = book;
		instantiateBookPanel("Update Book Info:", this.createSubmitButton());
		
		// instantiate the field so there will be enough fields for tags and author
		setUpFieldEnvirnment(book);
		
		// Fill field with info
		fillBookInfo(book);
		
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
				
				DatabaseConnectionBookApi.compareAndUpdateBookInfo(oldBookInfo, newBook);
				HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());

				
			}
		});
		
		return submitBtn;
	}
	
}
