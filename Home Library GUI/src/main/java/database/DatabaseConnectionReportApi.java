package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import app.HL_xiewen4;
import database.tables.BookAuthorTable;
import database.tables.BookKeywordTable;
import database.tables.BookTable;
import items.Person;

/**
 * @author xiewen4
 * This api is use to generate reports
 */
public class DatabaseConnectionReportApi extends DatabaseConnectionApi {
	
	/************************
	 * Author's Publication *
	 ************************/
	
	/**
	 * Find all the books that’s been 
	 * written by one author.
	 * @param authorName
	 * @return a HashMap {(column Name : column Data)}
	 */
	public static HashMap<String, ArrayList<String>> generateAuthorsPublication(String authorName) {
		HashMap<String, ArrayList<String>> result = null;
		try {
			// Disable auto commit
			disableAutoCommit();
			
			result = generateAuthorsPublicationHelper(authorName);
			
			// commit
			sqlCommit();
			
			// enable auto commit
			enableAutoCommit();
		
		} catch (Exception e) {
			// roll back
			try {
				sqlRollBack();
				enableAutoCommit();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static HashMap<String, ArrayList<String>> generateAuthorsPublicationHelper(String authorName) throws SQLException {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		result.put("ISBN", new ArrayList<>());
		result.put("Book's Name", new ArrayList<>());
		result.put("Published year", new ArrayList<>());
		
		Person author = formatNameToPerson(authorName);
		Integer authorID = tryToFindPerson(author);
		
		if (authorID != null) {
			try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
				
				String bookAuthorQuery =
						"(SELECT * FROM " + BookAuthorTable.TABLE_NAME + " "
						+ "WHERE " + BookAuthorTable.AUTHOR_ID + " = ?) AS ba";
				String query =
						"SELECT " + BookTable.ISBN + ", "
								+ BookTable.TITLE + ", "
								+ BookTable.YEAR_OF_PUBLICATION + " "
						+ "FROM "
							+ BookTable.TABLE_NAME + " "
							+ "NATURAL JOIN " + bookAuthorQuery;
				
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setInt(1, authorID);
				ResultSet rs = ps.executeQuery();
				
				while (rs.next()) {
					result.get("ISBN").add(rs.getString(BookTable.ISBN));
					result.get("Book's Name").add(rs.getString(BookTable.TITLE));
					result.get("Published year").add(rs.getString(BookTable.YEAR_OF_PUBLICATION));
				}
				
				connection.close();
			} catch (SQLException e) {
			    throw new SQLException(e);
			}
			
		} else {
			JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Author not found.");
		}
		
		return result;
			
	}
	
	/**
	 * Format the name give into a person object
	 * @param name
	 * @return
	 */
	private static Person formatNameToPerson(String name) {
		// Split author's name
		name = name.trim();
		String[] nameList = name.split(" ");
		ArrayList<String> newList = new ArrayList<>();
		for (String e : nameList) {
			if (e.length() != 0)
				newList.add(e);
		}
		
		Person person = null;
		if (newList.size() >= 3) {
			person = new Person(newList.get(0), newList.get(2));
			person.setMiddleName(newList.get(2));
		} else if (newList.size() >= 2) {
			person = new Person(newList.get(0), newList.get(1));
		}
		
		return person;
	}
}
