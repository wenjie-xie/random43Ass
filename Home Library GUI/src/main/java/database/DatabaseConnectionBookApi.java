package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
				System.out.println("INSERT INTO " + BookKeywordTable.TABLE_NAME + " "
						+ "(" + BookKeywordTable.ISBN + ", " + BookKeywordTable.KEYWORD_ID + ") "
						+ "VALUES (" + book.getBookISBN() + ", " + tagID + ")");
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
			System.out.println("SELECT * FROM " + KeywordTable.TABLE_NAME + " "
					+ "WHERE " + KeywordTable.TAG + " = " + tag + "");
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
			System.out.println("INSERT INTO " + KeywordTable.TABLE_NAME + " "
					+ "(" + KeywordTable.TAG + ") "
					+ "VALUES (" + tag + ")");
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
				System.out.println("INSERT INTO " + BookAuthorTable.TABLE_NAME + " "
						+ "(" + BookAuthorTable.ISBN + ", " + BookAuthorTable.AUTHOR_ID + ") "
						+ "VALUES (" + book.getBookISBN() + ", " + authorID + ")");
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
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + BookTable.TABLE_NAME + " WHERE " + BookTable.TITLE + " = '" + bookName + "'");
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + BookTable.TABLE_NAME + " WHERE " + BookTable.TITLE + " = '" + bookName + "'");
			if (rs.next()) {
				result = rs.getString(BookTable.ISBN);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
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
		Book book;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + BookTable.TABLE_NAME + " WHERE " + BookTable.TITLE + " = '" + bookName + "'");
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + BookTable.TABLE_NAME + " WHERE " + BookTable.TITLE + " = '" + bookName + "'");
			
			String bookISBN = rs.getString(BookTable.ISBN);
			String bookTitle = rs.getString(BookTable.TITLE);
			String bookPublisher = rs.getString(BookTable.PUBLISHER);
			int bookNumberOfPages = Integer.parseInt(rs.getString(BookTable.NUMBER_OF_PAGES));
			int bookYear = Integer.parseInt(rs.getString(BookTable.YEAR_OF_PUBLICATION));
			int bookEdition = Integer.parseInt(rs.getString(BookTable.EDITION_NUMBER));
			String bookAbstract = rs.getString(BookTable.ABSTRACT);
			
			book = new Book(bookISBN, bookTitle, bookPublisher, bookNumberOfPages, bookYear);
			book.setEditionNumber(bookEdition);
			book.setBookDescription(bookAbstract);
			
			book.setAuthorList(getAuthorList(bookISBN));
			book.setKeyWords(getTagList(bookISBN));
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return book;
	}
	
	
	/**
	 * Get Tag from Keyword table
	 * @param id the id of the keyword
	 * @return the tag name
	 * @throws SQLException 
	 */
	private static String getTagFromKeywordTable(int id) throws SQLException {
		String tag;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + KeywordTable.TABLE_NAME + " WHERE " + KeywordTable.ID + " = " + id);
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + KeywordTable.TABLE_NAME + " WHERE " + KeywordTable.ID + " = " + id);
			tag = rs.getString(KeywordTable.TAG);
			
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
				tagList.add(getTagFromKeywordTable(Integer.parseInt(tagID)));
			}
			
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
				authorList.add(getPersonFromPeopleInvolvedTable(Integer.parseInt(authorID)));
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return authorList;
	}
	
	
	/**************************************
	 * UPDATE BOOK *
	 **************************************/
	
	/**
	 * The purpose of this function is to update the book info for the old
	 * book with the new book by changing the database
	 * @param oldBookInfo
	 * @param newBookInfo
	 * @throws SQLException 
	 */
	public static void compareAndUpdateBookInfo(Book oldBookInfo, Book newBookInfo) throws SQLException {
		// compare and update Book table
		compareAndUpdateBookTable(oldBookInfo, newBookInfo);
		
		// compare and update Book Author Table
		compareAndUpdateBookAuthorTable(oldBookInfo, newBookInfo);
		
		// compare and update Book Keyword Table
		compareAndUpdateBookKeywordTable(oldBookInfo, newBookInfo);
	}
	
	/**
	 * Update the book table with the new data
	 * @param oldBookInfo
	 * @param columnName
	 * @param newData the new data with ''
	 * @throws SQLException 
	 */
	private static void updateBookTableWithNewData(Book oldBookInfo, String columnName, String newData) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE " + BookTable.TABLE_NAME + " "
					+ "SET " + columnName + " = " + newData + " "
					+ "WHERE " + BookTable.ISBN + " = " + oldBookInfo.getBookISBN() + " "
					+ "and " + BookTable.TITLE + " = " + oldBookInfo.getBookName());
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Update the book table with the new data
	 * @param oldBookInfo
	 * @param columnName
	 * @param newData the new data as a int
	 * @throws SQLException 
	 */
	private static void updateBookTableWithNewData(Book oldBookInfo, String columnName, int newData) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE " + BookTable.TABLE_NAME + " "
					+ "SET " + columnName + " = " + newData + " "
					+ "WHERE " + BookTable.ISBN + " = " + oldBookInfo.getBookISBN() + " "
					+ "and " + BookTable.TITLE + " = " + oldBookInfo.getBookName());
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Compare and update Book Table
	 * @param oldBookInfo
	 * @param newBookInfo
	 * @throws SQLException 
	 */
	private static void compareAndUpdateBookTable(Book oldBookInfo, Book newBookInfo) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			// check ISBN
			if (!oldBookInfo.getBookISBN().equals(newBookInfo.getBookISBN())) {
				updateBookTableWithNewData(oldBookInfo, BookTable.ISBN, newBookInfo.getBookISBN());
			}
			
			// check Title
			if (!oldBookInfo.getBookName().equals(newBookInfo.getBookName())) {
				updateBookTableWithNewData(oldBookInfo, BookTable.TITLE, newBookInfo.getBookName());
			}
			
			// check publisher
			if (!oldBookInfo.getPublisherName().equals(newBookInfo.getPublisherName())) {
				updateBookTableWithNewData(oldBookInfo, BookTable.PUBLISHER, newBookInfo.getPublisherName());
			}
			
			// check number of page
			if (!oldBookInfo.getNumOfPage().equals(newBookInfo.getNumOfPage())) {
				updateBookTableWithNewData(oldBookInfo, BookTable.NUMBER_OF_PAGES, Integer.parseInt(newBookInfo.getNumOfPage()));
			}
			
			// check year of publication
			if (!oldBookInfo.getPublicationYear().equals(newBookInfo.getPublicationYear())) {
				updateBookTableWithNewData(oldBookInfo, BookTable.YEAR_OF_PUBLICATION, Integer.parseInt(newBookInfo.getPublicationYear()));
			}
			
			// check edition number
			if (!oldBookInfo.getEditionNumber().equals(newBookInfo.getEditionNumber())) {
				updateBookTableWithNewData(oldBookInfo, BookTable.EDITION_NUMBER, Integer.parseInt(newBookInfo.getEditionNumber()));
			}
			
			// check abstract
			if (!oldBookInfo.getBookDescription().equals(newBookInfo.getBookDescription())) {
				updateBookTableWithNewData(oldBookInfo, BookTable.ABSTRACT, newBookInfo.getBookDescription());
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Update the book author table with the new data
	 * @param oldBookInfo
	 * @param columnName
	 * @param newData the new data with ''
	 * @throws SQLException 
	 */
	private static void updateBookAuthorTableWithNewData(Book oldBookInfo, String columnName, String newData) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE " + BookAuthorTable.TABLE_NAME + " "
					+ "SET " + columnName + " = " + newData + " "
					+ "WHERE " + BookAuthorTable.ISBN + " = " + oldBookInfo.getBookISBN());
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Compare and update the data in book author
	 * @param oldBookInfo
	 * @param newBookInfo
	 * @throws SQLException 
	 */
	private static void compareAndUpdateBookAuthorTable(Book oldBookInfo, Book newBookInfo) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			if (!oldBookInfo.getBookISBN().equals(newBookInfo.getBookISBN())) {
				updateBookAuthorTableWithNewData(oldBookInfo, BookAuthorTable.ISBN, newBookInfo.getBookISBN());
			}
			
			ArrayList<Person> newAuthorList = newBookInfo.getAuthorList();
			// check author id
			// Remember the book iSBN is change above
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT " + BookAuthorTable.AUTHOR_ID + " "
					+ "FROM " + BookAuthorTable.TABLE_NAME + " "
					+ "WHERE " + BookAuthorTable.ISBN + " = " + newBookInfo.getBookISBN());
			
			// check each author id
			ArrayList<Integer> authorIDThatShouldBeRemove = new ArrayList<>();
			while (rs.next()) {
				String authorID = rs.getString(BookAuthorTable.AUTHOR_ID);
				Person oldAuthor = getPersonFromPeopleInvolvedTable(Integer.parseInt(authorID));
				
				// if author exists in newAuthorList then remove the author from newAuthorList
				if (newAuthorList.contains(oldAuthor)) {
					newAuthorList.remove(oldAuthor);
					
				// if the author does not exist anymore then append the id to the authorIDThatShouldBeRemove
				} else {
					authorIDThatShouldBeRemove.add(Integer.parseInt(authorID));
				}
			}
			
			// add the new author into book
			Book newBookClone = new Book(newBookInfo.getBookISBN(),
					newBookInfo.getBookName(),
					newBookInfo.getPublisherName(),
					Integer.parseInt(newBookInfo.getNumOfPage()),
					Integer.parseInt(newBookInfo.getPublicationYear()));
			newBookClone.setAuthorList(newAuthorList);
			insertIntoBookAuthor(newBookClone);
			
			// Remove the authors that no longer exists
			for (int authorID : authorIDThatShouldBeRemove) {
				removeAuthorFromBookAuthor(newBookInfo.getBookISBN(), authorID);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Update the book keyword table with the new data
	 * @param oldBookInfo
	 * @param columnName
	 * @param newData the new data with ''
	 * @throws SQLException 
	 */
	private static void updateBookKeywordTableWithNewData(Book oldBookInfo, String columnName, String newData) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE " + BookKeywordTable.TABLE_NAME + " "
					+ "SET " + columnName + " = " + newData + " "
					+ "WHERE " + BookKeywordTable.ISBN + " = " + oldBookInfo.getBookISBN());
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Compare and update the data in book keyword
	 * @param oldBookInfo
	 * @param newBookInfo
	 * @throws SQLException 
	 */
	private static void compareAndUpdateBookKeywordTable(Book oldBookInfo, Book newBookInfo) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			if (!oldBookInfo.getBookISBN().equals(newBookInfo.getBookISBN())) {
				updateBookKeywordTableWithNewData(oldBookInfo, BookAuthorTable.ISBN, newBookInfo.getBookISBN());
			}
			
			ArrayList<String> newKeywordList = newBookInfo.getKeyWords();
			// check author id
			// Remember the book iSBN is change above
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT " + BookKeywordTable.KEYWORD_ID + " "
					+ "FROM " + BookKeywordTable.TABLE_NAME + " "
					+ "WHERE " + BookKeywordTable.ISBN + " = " + newBookInfo.getBookISBN());
			
			// check each keyword id
			ArrayList<Integer> keywordIDThatShouldBeRemove = new ArrayList<>();
			while (rs.next()) {
				String keywordID = rs.getString(BookKeywordTable.KEYWORD_ID);
				String oldTag = getTagFromKeywordTable(Integer.parseInt(keywordID));
				
				// if oldTag exists in newKeywordList then remove the oldTag from newKeywordList
				if (newKeywordList.contains(oldTag)) {
					newKeywordList.remove(oldTag);
					
				// if the oldTag does not exist anymore then append the id to the keywordIDThatShouldBeRemove
				} else {
					keywordIDThatShouldBeRemove.add(Integer.parseInt(keywordID));
				}
			}
			
			// add the new keyword into book
			Book newBookClone = new Book(newBookInfo.getBookISBN(),
					newBookInfo.getBookName(),
					newBookInfo.getPublisherName(),
					Integer.parseInt(newBookInfo.getNumOfPage()),
					Integer.parseInt(newBookInfo.getPublicationYear()));
			newBookClone.setKeyWords(newKeywordList);
			insertIntoBookKeyword(newBookClone);
			
			// Remove the oldTag ids that no longer exists
			for (int keywordID : keywordIDThatShouldBeRemove) {
				removeKeywordFromBookAuthor(newBookInfo.getBookISBN(), keywordID);
			}
			
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
	private static void removeAuthorFromBookAuthor(String bookISBN, int authorID) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("DELETE FROM " + BookAuthorTable.TABLE_NAME + " "
					+ "WHERE " + BookAuthorTable.ISBN + " = " + bookISBN + " "
					+ "and " + BookAuthorTable.AUTHOR_ID + " = " + authorID);
			
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
	private static void removeKeywordFromBookAuthor(String bookISBN, int keywordID) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("DELETE FROM " + BookKeywordTable.TABLE_NAME + " "
					+ "WHERE " + BookKeywordTable.ISBN + " = " + bookISBN + " "
					+ "and " + BookKeywordTable.KEYWORD_ID + " = " + keywordID);
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
}
