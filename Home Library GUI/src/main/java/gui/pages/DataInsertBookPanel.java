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
import gui.functions.BookPageInsertFunctions;
import items.Book;
import items.Person;

/**
 * @author xiewen4
 * 
 */
public class DataInsertBookPanel extends BookPageInsertFunctions {

	private static final long serialVersionUID = 2059922932338622502L;

	public DataInsertBookPanel() {
		instantiateBookPanel("Add New Book:" , this.createSubmitButton());
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
				Book book = getBookInfo();
				
				DatabaseConnectionBookApi.insertBook(book);
				HL_xiewen4.mainFrame.flipPageTo(new HomePagePanel());
				
			}
		});
		
		return submitBtn;
	}
}
