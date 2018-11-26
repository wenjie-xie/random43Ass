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
			
			// compare and update Book Author table
			updateBookAuthorTable(oldBookInfo, newBookInfo);
			
			// compare and update Book keyword table
			updateBookKeywordTable(oldBookInfo, newBookInfo);
			
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
	 * Update the Book author table
	 * @param oldBookInfo
	 * @param newBookInfo
	 * @throws SQLException
	 */
	private static void updateBookAuthorTable(Book oldBookInfo, Book newBookInfo) throws SQLException {
		HashMap<String, ArrayList<Person>> removeUpdateAndNewMap = determineRemovedUpdateAndNewPerson(
				oldBookInfo.getAuthorList(),
				newBookInfo.getAuthorList());
		ArrayList<Person> removedPeopleList = removeUpdateAndNewMap.get("removed");
		ArrayList<Person> updatePeopleList = removeUpdateAndNewMap.get("updated");
		ArrayList<Person> newPeopleList = removeUpdateAndNewMap.get("new");
		
		// update
		for (int updatePersonIndex = 0; updatePersonIndex < updatePeopleList.size(); updatePersonIndex++) {
			Person person = updatePeopleList.get(updatePersonIndex);
			Person newPersonInfo = newBookInfo.getAuthorList().get(updatePersonIndex);
			
			if (!person.equals(newPersonInfo)) {
				int personID = tryToFindPerson(person);
				updatePersonWithID(personID, newPersonInfo);
			}
		}
		
		// remove
		for (int removePersonIndex = 0; removePersonIndex < removedPeopleList.size(); removePersonIndex++) {
			Person person = removedPeopleList.get(removePersonIndex);
			int personID = tryToFindPerson(person);
			String isbn = formatString(oldBookInfo.getBookISBN());
			removeAuthorFromBookAuthor(isbn, personID);
		}
		
		// add
		Book dummy = new Book(formatString(newBookInfo.getBookISBN()),
				null, null, null, null);
		dummy.setAuthorList(newPeopleList);
		insertIntoBookAuthor(dummy);
		
		// update ISBN
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE " + BookAuthorTable.TABLE_NAME + " "
					+ "SET " + BookAuthorTable.ISBN + " = " + newBookInfo.getBookISBN() + " "
					+ "WHERE " + BookAuthorTable.ISBN + " = " + oldBookInfo.getBookISBN());
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * The purpose of this method is to update the tag with the id given to
	 * the newTagInfo in the Keyword Table
	 * @param keywordID the id of the keyword you are trying to update, 
	 * @param newTagInfo the info you are trying to update the tag into
	 * @throws SQLException 
	 */
	private static void updateKeywordWithID(int keywordID, String newTag) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE " + KeywordTable.TABLE_NAME + " "
					+ "SET " + KeywordTable.TAG + " = '" + newTag + "' "
					+ "WHERE " + KeywordTable.ID + " = " + keywordID);
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Determine the tags that needs to be removed, update or added from the two given tag list
	 * @param oldTagList
	 * @param newTagList
	 * @return a map to each of the three list with keys "removed", "update", and "new"
	 */
	protected static HashMap<String, ArrayList<String>> determineRemovedUpdateAndNewTags(ArrayList<String> oldTagList, ArrayList<String> newTagList) {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		// old tags that needs to be removed
		ArrayList<String> removedTagList = new ArrayList<>();
		// new tags that needs to be added
		ArrayList<String> addTagList = new ArrayList<>();
		// old tags that needs to be updated
		ArrayList<String> updateTagList = new ArrayList<>();
		
		
		// if some tags are deleted
		if (newTagList.size() <= oldTagList.size()) {
			for (int i = 0; i < newTagList.size(); i++) {
				String needUpdate = oldTagList.get(i);
				updateTagList.add(needUpdate);
			}
			
			for (int i = newTagList.size(); i < oldTagList.size(); i++) {
				String needRemove = oldTagList.get(i);
				removedTagList.add(needRemove);
			}
			
		// if some tags are added
		} else {
			for (int i = 0; i < oldTagList.size(); i++) {
				String needUpdate = oldTagList.get(i);
				updateTagList.add(needUpdate);
			}
			
			for (int i = oldTagList.size(); i < newTagList.size(); i++) {
				String needAdd = newTagList.get(i);
				addTagList.add(needAdd);
			}
		}
		
		
		result.put("removed", removedTagList);
		result.put("new", addTagList);
		result.put("update", updateTagList);
		
		return result;
	}
	
	/**
	 * Update the Book keyword table
	 * @param oldBookInfo
	 * @param newBookInfo
	 * @throws SQLException 
	 */
	private static void updateBookKeywordTable(Book oldBookInfo, Book newBookInfo) throws SQLException {
		HashMap<String, ArrayList<String>> removeUpdateAndNewMap = determineRemovedUpdateAndNewTags(
				oldBookInfo.getKeyWords(),
				newBookInfo.getKeyWords());
		ArrayList<String> removedTagList = removeUpdateAndNewMap.get("removed");
		ArrayList<String> updateTagList = removeUpdateAndNewMap.get("update");
		ArrayList<String> newTagList = removeUpdateAndNewMap.get("new");
		
		// update
		for (int updateTagIndex = 0; updateTagIndex < updateTagList.size(); updateTagIndex++) {
			String tag = updateTagList.get(updateTagIndex);
			String newTagInfo = newBookInfo.getKeyWords().get(updateTagIndex);
			
			if (!tag.equals(newTagInfo)) {
				int tagID = tryToFindBookTag(tag);
				updateKeywordWithID(tagID, newTagInfo);
			}
		}
		
		// remove
		for (int removeTagIndex = 0; removeTagIndex < removedTagList.size(); removeTagIndex++) {
			String tag = removedTagList.get(removeTagIndex);
			int tagID = tryToFindBookTag(tag);
			String isbn = formatString(oldBookInfo.getBookISBN());
			removeKeywordFromBookKeyword(isbn, tagID);
		}
		
		// add
		Book dummy = new Book(formatString(newBookInfo.getBookISBN()),
				null, null, null, null);
		dummy.setKeyWords(newTagList);
		insertIntoBookKeyword(dummy);
		
		// update ISBN
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE " + BookKeywordTable.TABLE_NAME + " "
					+ "SET " + BookKeywordTable.ISBN + " = " + newBookInfo.getBookISBN() + " "
					+ "WHERE " + BookKeywordTable.ISBN + " = " + oldBookInfo.getBookISBN());
			
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
	 * @param bookISBN with no ''
	 * @param authorID
	 * @throws SQLException 
	 */
	private static void removeAuthorFromBookAuthor(String bookISBN, int authorID) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("DELETE FROM " + BookAuthorTable.TABLE_NAME + " "
					+ "WHERE " + BookAuthorTable.ISBN + " = '" + bookISBN + "' "
					+ "and " + BookAuthorTable.AUTHOR_ID + " = " + authorID);
			stmt.executeUpdate("DELETE FROM " + BookAuthorTable.TABLE_NAME + " "
					+ "WHERE " + BookAuthorTable.ISBN + " = '" + bookISBN + "' "
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
