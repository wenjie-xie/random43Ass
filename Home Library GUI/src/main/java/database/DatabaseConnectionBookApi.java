package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.tables.BookAuthorTable;
import database.tables.BookKeywordTable;
import database.tables.BookTable;
import database.tables.KeywordTable;
import database.tables.PeopleInvolvedTable;
import items.Book;
import items.Person;

/**
 * @author xiewen4
 * All the functions related to Book in the Database
 */
public class DatabaseConnectionBookApi extends DatabaseConnectionApi {
	
	/****************************************
	 * INSERT BOOK *
	 *****************************************/
	
	/**
	 * Insert a new Book
	 * @param book takes in a book object containing all the book info from the front end
	 * @throws SQLException 
	 */
	public static void insertBook(Book book) throws SQLException {
		try {
			// insert into Book table
			insertIntoBook(book);
			
			// insert into the Book Keyword table
			insertIntoBookKeyword(book);
			
			// insert into the BookAuthor
			insertIntoBookAuthor(book);
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Fill book info into Book table
	 * @param book
	 * @throws SQLException 
	 */
	private static void insertIntoBook(Book book) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("INSERT INTO " + BookTable.TABLE_NAME + " "
					+ "(" + BookTable.ISBN + ", " + BookTable.TITLE + ", " + BookTable.PUBLISHER + ", "
					+ BookTable.NUMBER_OF_PAGES + ", " + BookTable.YEAR_OF_PUBLICATION + ", "
					+ BookTable.EDITION_NUMBER + ", " + BookTable.ABSTRACT + ") "
					+ "VALUES (" + book.getBookISBN() + ", " + book.getBookName() + ", " + book.getPublisherName() + ", "
					+ book.getNumOfPage() + ", " + book.getPublicationYear() + ", "
					+ book.getEditionNumber() + ", " + book.getBookDescription() + ")");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Fill book info into BookKeyword Table
	 * @param book a object that contains all the book info from the front end
	 * @throws SQLException 
	 */
	private static void insertIntoBookKeyword(Book book) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			// Loop through each tag
			for (String tag : book.getKeyWords()) {
				tag = "'" + tag + "'";
				String tagID = tryToFindBookTag(tag);
				
				// If tag does not exist then add it into the Keyword table
				if (tagID == null) {
					insertIntoKeyword(tag);
					tagID = tryToFindBookTag(tag);	// Grab the id now that it is in the Keyword table
				}
				
				// SQL
				Statement stmt = null;
				stmt = connection.createStatement();
				stmt.executeUpdate("INSERT INTO " + BookKeywordTable.TABLE_NAME + " "
						+ "(" + BookKeywordTable.ISBN + ", " + BookKeywordTable.KEYWORD_ID + ") "
						+ "VALUES (" + book.getBookISBN() + ", " + tagID + ")");
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Try to find the ID for the given tag
	 * @param tag the tag that is being searched
	 * @return true iff the tag already exists
	 * @throws SQLException 
	 */
	private static String tryToFindBookTag(String tag) throws SQLException {
		String result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + KeywordTable.TABLE_NAME + " "
					+ "WHERE " + KeywordTable.TAG + " = " + tag + "");
			if (rs.next()) {
				result = rs.getString(KeywordTable.ID);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	/**
	 * Fill book info into Keyword Table
	 * @param tag the new tag that is going to be added into the database
	 * @throws SQLException 
	 */
	private static void insertIntoKeyword(String tag) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("INSERT INTO " + KeywordTable.TABLE_NAME + " "
					+ "(" + KeywordTable.TAG + ") "
					+ "VALUES (" + tag + ")");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Fill book info into BookAuthor table
	 * @param book
	 * @throws SQLException 
	 */
	private static void insertIntoBookAuthor(Book book) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			// Insert each author in book into the database
			for (Person author : book.getAuthorList()) {
				// try to grab the author ID from the database
				// if the author is new insert the author to the database
				String authorID = findOrCreatePerson(author);
				
				Statement stmt = null;
				stmt = connection.createStatement();
				stmt.executeUpdate("INSERT INTO " + BookAuthorTable.TABLE_NAME + " "
						+ "(" + BookAuthorTable.ISBN + ", " + BookAuthorTable.AUTHOR_ID + ") "
						+ "VALUES (" + book.getBookISBN() + ", " + authorID + ")");
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Try to find the ID for the book author
	 * @param author
	 * @return ID of the author iff the author already exists, else return null
	 * @throws SQLException 
	 *//*
	private static String tryToFindAuthor(Person author) throws SQLException {
		String result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = " + author.getSurname() + " "
							+ "and " + PeopleInvolvedTable.FIRST_NAME + " = " + author.getFirstName() + " "
							+ "and " + PeopleInvolvedTable.MIDDLE_NAME + " = " + author.getMiddleName() + "");
			if (rs.next()) {
				result = rs.getString(PeopleInvolvedTable.ID);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	*//**
	 * Add a new person to PeopleInvolved
	 * @param author the new author that is going to be put into the database
	 * @throws SQLException 
	 *//*
	private static void insertIntoPeopleInvolved(Person author) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("INSERT INTO " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "(" + PeopleInvolvedTable.FIRST_NAME + ", " + PeopleInvolvedTable.MIDDLE_NAME + ", "
						+ PeopleInvolvedTable.FAMILY_NAME + ", " + PeopleInvolvedTable.GENDER + ") "
					+ "VALUES (" + author.getFirstName() + ", " + author.getMiddleName() + ", "
							+ author.getSurname() + ", " + author.getGender() + ")");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}*/
	
	
	/***********************************
	 * UPDATE BOOK *
	 ***********************************/
	
	/**
	 * Try to find the book ISBN with the given book Name
	 * @param bookTitle the title of the book being searched
	 * @return the ISBN of the book iff the book title exists in the database, otherwise return null
	 * @throws SQLException 
	 */
	public static String tryToFindBookName(String bookName) throws SQLException {
		String result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + BookTable.TABLE_NAME + " WHERE " + BookTable.TITLE + " = '" + bookName + "'");
			if (rs.next()) {
				result = rs.getString(BookTable.ISBN);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
}
