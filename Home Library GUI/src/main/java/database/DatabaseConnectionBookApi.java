package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
	public static void insertBook(Book book) {

		try {
			// Disable auto commit
			disableAutoCommit();
			
			// insert into Book table
			insertIntoBook(book);
		
			// insert into the Book Keyword table
			insertIntoBookKeyword(book);
			
			// insert into the BookAuthor
			insertIntoBookAuthor(book);
			
			// commit
			sqlCommit();
			
			// enable auto commit
			enableAutoCommit();
		
		} catch (SQLException e) {
			// roll back
			try {
				sqlRollBack();
				enableAutoCommit();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
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
			System.out.println("INSERT INTO " + BookTable.TABLE_NAME + " "
					+ "(" + BookTable.ISBN + ", " + BookTable.TITLE + ", " + BookTable.PUBLISHER + ", "
					+ BookTable.NUMBER_OF_PAGES + ", " + BookTable.YEAR_OF_PUBLICATION + ", "
					+ BookTable.EDITION_NUMBER + ", " + BookTable.ABSTRACT + ") "
					+ "VALUES (" + book.getBookISBN() + ", " + book.getBookName() + ", " + book.getPublisherName() + ", "
					+ book.getNumOfPage() + ", " + book.getPublicationYear() + ", "
					+ book.getEditionNumber() + ", " + book.getBookDescription() + ")");
			stmt.executeUpdate("INSERT INTO " + BookTable.TABLE_NAME + " "
					+ "(" + BookTable.ISBN + ", " + BookTable.TITLE + ", " + BookTable.PUBLISHER + ", "
					+ BookTable.NUMBER_OF_PAGES + ", " + BookTable.YEAR_OF_PUBLICATION + ", "
					+ BookTable.EDITION_NUMBER + ", " + BookTable.ABSTRACT + ") "
					+ "VALUES (" + book.getBookISBN() + ", " + book.getBookName() + ", " + book.getPublisherName() + ", "
					+ book.getNumOfPage() + ", " + book.getPublicationYear() + ", "
					+ book.getEditionNumber() + ", " + book.getBookDescription() + ")");
			connection.close();
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
				Integer tagID = findOrCreateBookTag(tag);
				
				// SQL
				Statement stmt = null;
				stmt = connection.createStatement();
				System.out.println("INSERT INTO " + BookKeywordTable.TABLE_NAME + " "
						+ "(" + BookKeywordTable.ISBN + ", " + BookKeywordTable.KEYWORD_ID + ") "
						+ "VALUES (" + book.getBookISBN() + ", " + tagID + ")");
				stmt.executeUpdate("INSERT INTO " + BookKeywordTable.TABLE_NAME + " "
						+ "(" + BookKeywordTable.ISBN + ", " + BookKeywordTable.KEYWORD_ID + ") "
						+ "VALUES (" + book.getBookISBN() + ", " + tagID + ")");
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	private static Integer findOrCreateBookTag(String tag) throws SQLException {
		Integer tagID = tryToFindBookTag(tag);
		
		// If tag does not exist then add it into the Keyword table
		if (tagID == null) {
			insertIntoKeyword(tag);
			tagID = tryToFindBookTag(tag);	// Grab the id now that it is in the Keyword table
		}
		
		return tagID;
	}
	
	/**
	 * Try to find the ID for the given tag
	 * @param tag the tag that is being searched
	 * @return tag id iff the tag already exists
	 * @throws SQLException 
	 */
	private static Integer tryToFindBookTag(String tag) throws SQLException {
		Integer result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + KeywordTable.TABLE_NAME + " "
					+ "WHERE " + KeywordTable.TAG + " = '" + tag + "'");
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + KeywordTable.TABLE_NAME + " "
					+ "WHERE " + KeywordTable.TAG + " = '" + tag + "'");
			if (rs.next()) {
				result = formatStringToInt(rs.getString(KeywordTable.ID));
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	/**
	 * Fill book info into Keyword Table
	 * @param tag the new tag that is going to be added into the database, with no "'"
	 * @throws SQLException 
	 */
	private static void insertIntoKeyword(String tag) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("INSERT INTO " + KeywordTable.TABLE_NAME + " "
					+ "(" + KeywordTable.TAG + ") "
					+ "VALUES ('" + tag + "')");
			stmt.executeUpdate("INSERT INTO " + KeywordTable.TABLE_NAME + " "
					+ "(" + KeywordTable.TAG + ") "
					+ "VALUES ('" + tag + "')");
			
			connection.close();
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
				Integer authorID = findOrCreatePerson(author);
				
				Statement stmt = null;
				stmt = connection.createStatement();
				System.out.println("INSERT INTO " + BookAuthorTable.TABLE_NAME + " "
						+ "(" + BookAuthorTable.ISBN + ", " + BookAuthorTable.AUTHOR_ID + ") "
						+ "VALUES (" + book.getBookISBN() + ", " + authorID + ")");
				stmt.executeUpdate("INSERT INTO " + BookAuthorTable.TABLE_NAME + " "
						+ "(" + BookAuthorTable.ISBN + ", " + BookAuthorTable.AUTHOR_ID + ") "
						+ "VALUES (" + book.getBookISBN() + ", " + authorID + ")");
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/***********************************
	 * SEARCH BOOK *
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
			// disable auto commit
			disableAutoCommit();
			
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + BookTable.TABLE_NAME + " WHERE " + BookTable.TITLE + " = '" + bookName + "'");
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + BookTable.TABLE_NAME + " WHERE " + BookTable.TITLE + " = '" + bookName + "'");
			if (rs.next()) {
				result = rs.getString(BookTable.ISBN);
			}
			
			connection.close();
			
			// commit
			sqlCommit();
			
			// enable auto commit
			enableAutoCommit();
		
		} catch (SQLException e) {
			// roll back
			try {
				sqlRollBack();
				enableAutoCommit();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/*******************************
	 * GET BOOK *
	 *******************************/
	
	/**
	 * Get all info of a given Book Name into a Book object
	 * @param bookName the name of the book that you are trying to get info from
	 * @return a book object
	 * @throws SQLException 
	 */
	public static Book getBookInfo(String bookName) throws SQLException {
		Book book = null;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			// disable auto commit
			disableAutoCommit();
			
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + BookTable.TABLE_NAME + " WHERE " + BookTable.TITLE + " = '" + bookName + "'");
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + BookTable.TABLE_NAME + " WHERE " + BookTable.TITLE + " = '" + bookName + "'");
			
			rs.next();
			String bookISBN = rs.getString(BookTable.ISBN);
			String bookTitle = rs.getString(BookTable.TITLE);
			String bookPublisher = rs.getString(BookTable.PUBLISHER);
			Integer bookNumberOfPages = formatStringToInt(rs.getString(BookTable.NUMBER_OF_PAGES));
			Integer bookYear = formatStringToInt(rs.getString(BookTable.YEAR_OF_PUBLICATION));
			Integer bookEdition = formatStringToInt(rs.getString(BookTable.EDITION_NUMBER));
			String bookAbstract = rs.getString(BookTable.ABSTRACT);
			
			book = new Book(bookISBN, bookTitle, bookPublisher, bookNumberOfPages, bookYear);
			book.setEditionNumber(bookEdition);
			book.setBookDescription(bookAbstract);
			
			book.setAuthorList(getAuthorList(bookISBN));
			book.setKeyWords(getTagList(bookISBN));
			
			connection.close();
			
			// commit
			sqlCommit();
			
			// enable auto commit
			enableAutoCommit();
		
		} catch (SQLException e) {
			// roll back
			try {
				sqlRollBack();
				enableAutoCommit();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		return book;
	}
	
	
	/**
	 * Get Tag from Keyword table
	 * @param id the id of the keyword
	 * @return the tag name
	 * @throws SQLException 
	 */
	private static String getTagFromKeywordTable(Integer id) throws SQLException {
		String tag;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + KeywordTable.TABLE_NAME + " WHERE " + KeywordTable.ID + " = " + id);
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + KeywordTable.TABLE_NAME + " WHERE " + KeywordTable.ID + " = " + id);
			rs.next();
			tag = rs.getString(KeywordTable.TAG);
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		return tag;
	}
	
	/**
	 * Get all the tag as a list with the given ISBN of the book
	 * @param bookISBN the isbn of the book
	 * @return a list of tags that belong to this book
	 * @throws SQLException 
	 */
	private static ArrayList<String> getTagList(String bookISBN) throws SQLException {
		ArrayList<String> tagList = new ArrayList<>();
		
		ArrayList<Integer> tagIDList = getTagIDList(bookISBN);
			
		for (Integer tagID : tagIDList) {
			tagList.add(getTagFromKeywordTable(tagID));
		}
		
		return tagList;
	}
	
	/**
	 * Get all the tag id as a list with the given ISBN of the book
	 * @param bookISBN the isbn of the book
	 * @return a list of tag id that belong to this book
	 * @throws SQLException 
	 */
	private static ArrayList<Integer> getTagIDList(String bookISBN) throws SQLException {
		ArrayList<Integer> tagList = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT " + BookKeywordTable.KEYWORD_ID + " "
					+ "FROM " + BookKeywordTable.TABLE_NAME + " "
					+ "WHERE " + BookKeywordTable.ISBN + " = '" + bookISBN + "'");
			ResultSet rs = stmt.executeQuery("SELECT " + BookKeywordTable.KEYWORD_ID + " "
					+ "FROM " + BookKeywordTable.TABLE_NAME + " "
					+ "WHERE " + BookKeywordTable.ISBN + " = '" + bookISBN + "'");
			
			while(rs.next()) {
				String tagID = rs.getString(BookKeywordTable.KEYWORD_ID);
				tagList.add(Integer.parseInt(tagID));
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return tagList;
	}
	
	/**
	 * Get all the author of the given book
	 * @param bookISBN
	 * @return a list of author
	 * @throws SQLException 
	 */
	private static ArrayList<Person> getAuthorList(String bookISBN) throws SQLException {
		ArrayList<Person> authorList = new ArrayList<>();
		
		ArrayList<Integer> authorIDList = getAuthorIDList(bookISBN);
		
		for (Integer authorID : authorIDList) {
			authorList.add(getPersonFromPeopleInvolvedTable(authorID));
		}
		
		return authorList;
	}
	
	
	/**
	 * Get all the author id of the given book
	 * @param bookISBN
	 * @return a list of author
	 * @throws SQLException 
	 */
	private static ArrayList<Integer> getAuthorIDList(String bookISBN) throws SQLException {
		ArrayList<Integer> authorIDList = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT " + BookAuthorTable.AUTHOR_ID + " "
					+ "FROM " + BookAuthorTable.TABLE_NAME + " "
					+ "WHERE " + BookAuthorTable.ISBN + " = '" + bookISBN + "'");
			ResultSet rs = stmt.executeQuery("SELECT " + BookAuthorTable.AUTHOR_ID + " "
					+ "FROM " + BookAuthorTable.TABLE_NAME + " "
					+ "WHERE " + BookAuthorTable.ISBN + " = '" + bookISBN + "'");
			
			while(rs.next()) {
				String authorID = rs.getString(BookAuthorTable.AUTHOR_ID);
				authorIDList.add(Integer.parseInt(authorID));
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return authorIDList;
	}
	
	
	/**************************************
	 * UPDATE BOOK *
	 **************************************/
	
	/**
	 * The purpose of this function is to update the book info for the old
	 * book with the new book by changing the database
	 * @param oldBookInfo
	 * @param newBookInfo
	 */
	public static void compareAndUpdateBookInfo(Book oldBookInfo, Book newBookInfo) {
	
		try {
			// disable auto commit
			disableAutoCommit();
			
			// compare and update Book table
			updateBookTable(oldBookInfo, newBookInfo);
			
			// commit
			sqlCommit();
			
			// enable auto commit
			enableAutoCommit();
	
		} catch (SQLException e) {
			// roll back
			try {
				sqlRollBack();
				enableAutoCommit();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Update Book Table
	 * @param oldBookInfo
	 * @param newBookInfo
	 * @throws SQLException 
	 */
	private static void updateBookTable(Book oldBookInfo, Book newBookInfo) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("UPDATE " + BookTable.TABLE_NAME + " "
					+ "SET " + BookTable.ISBN + " = " + newBookInfo.getBookISBN() + ", "
					+ BookTable.TITLE + " = " + newBookInfo.getBookName() + ", "
					+ BookTable.PUBLISHER + " = " + newBookInfo.getPublisherName() + ", "
					+ BookTable.NUMBER_OF_PAGES + " = " + newBookInfo.getNumOfPage() + ", "
					+ BookTable.YEAR_OF_PUBLICATION + " = " + newBookInfo.getPublicationYear() + ", "
					+ BookTable.EDITION_NUMBER + " = " + newBookInfo.getEditionNumber() + ", "
					+ BookTable.ABSTRACT + " = " + newBookInfo.getBookDescription() + " "
					+ "WHERE " + BookTable.ISBN + " = " + oldBookInfo.getBookISBN() + " "
					+ "and " + BookTable.TITLE + " = " + oldBookInfo.getBookName());
			stmt.executeUpdate("UPDATE " + BookTable.TABLE_NAME + " "
					+ "SET " + BookTable.ISBN + " = " + newBookInfo.getBookISBN() + ", "
					+ BookTable.TITLE + " = " + newBookInfo.getBookName() + ", "
					+ BookTable.PUBLISHER + " = " + newBookInfo.getPublisherName() + ", "
					+ BookTable.NUMBER_OF_PAGES + " = " + newBookInfo.getNumOfPage() + ", "
					+ BookTable.YEAR_OF_PUBLICATION + " = " + newBookInfo.getPublicationYear() + ", "
					+ BookTable.EDITION_NUMBER + " = " + newBookInfo.getEditionNumber() + ", "
					+ BookTable.ABSTRACT + " = " + newBookInfo.getBookDescription() + " "
					+ "WHERE " + BookTable.ISBN + " = " + oldBookInfo.getBookISBN() + " "
					+ "and " + BookTable.TITLE + " = " + oldBookInfo.getBookName());
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Alter BookAuthor Table to cascade ISBN on update
	 * @throws SQLException 
	 */
	private static void alterBookAuthorTableToOnUpdateCascade() throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("ALTER TABLE " + BookAuthorTable.TABLE_NAME + " "
					+ "ADD CONSTRAINT " + BookAuthorTable.ISBN + " "
					+ "FOREIGN KEY (" + BookAuthorTable.ISBN + ")" + " "
					+ "REFERENCES " + BookTable.TABLE_NAME + "(" + BookTable.ISBN + ")" + " "
					+ "ON UPDATE CASCADE");
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/****************************
	 * REMOVE BOOK *
	 ****************************/
	
	/**
	 * Remove the given row from book author
	 * @param bookISBN
	 * @param authorID
	 * @throws SQLException 
	 */
	private static void removeAuthorFromBookAuthor(String bookISBN, Integer authorID) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("DELETE FROM " + BookAuthorTable.TABLE_NAME + " "
					+ "WHERE " + BookAuthorTable.ISBN + " = " + bookISBN + " "
					+ "and " + BookAuthorTable.AUTHOR_ID + " = " + authorID);
			stmt.executeUpdate("DELETE FROM " + BookAuthorTable.TABLE_NAME + " "
					+ "WHERE " + BookAuthorTable.ISBN + " = " + bookISBN + " "
					+ "and " + BookAuthorTable.AUTHOR_ID + " = " + authorID);
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Remove the given row from book keyword
	 * @param bookISBN
	 * @param keywordID
	 * @throws SQLException 
	 */
	private static void removeKeywordFromBookKeyword(String bookISBN, Integer keywordID) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("DELETE FROM " + BookKeywordTable.TABLE_NAME + " "
					+ "WHERE " + BookKeywordTable.ISBN + " = " + bookISBN + " "
					+ "and " + BookKeywordTable.KEYWORD_ID + " = " + keywordID);
			stmt.executeUpdate("DELETE FROM " + BookKeywordTable.TABLE_NAME + " "
					+ "WHERE " + BookKeywordTable.ISBN + " = " + bookISBN + " "
					+ "and " + BookKeywordTable.KEYWORD_ID + " = " + keywordID);
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
}
