package database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import database.tables.BookTable;
import database.tables.KeywordTable;
import database.tables.PeopleInvolvedTable;
import items.Book;
import items.Person;

class DatabaseConnectionBookApiTest {
	protected static final String URL = "jdbc:mysql://localhost:3306/HL?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	protected static final String sqlUsername = "root";
	protected static final String sqlPassword = "a";

	@Test
	public void testInsertBook() throws SQLException {
		
		String isbn = "Test";
		String title = "Test123";
		String publisher = "jack";
		int numOfPage = 123;
		int publicationYear = 2000;
		int editionNum = 1;
		String description = "testing 123456";
		Book testBook = new Book(isbn, title, publisher, numOfPage, publicationYear);
		testBook.setEditionNumber(editionNum);
		testBook.setBookDescription(description);
		
		String firstName = "myFirstName";
		String lastName = "myLastName";
		testBook.setAuthorList(new ArrayList<>(Arrays.asList(
				new Person(lastName, firstName))));
		
		String tag = "testTag";
		testBook.setKeyWords(new ArrayList<>(Arrays.asList(tag)));
		DatabaseConnectionBookApi.insertBook(testBook);
		
		ArrayList<String> expectedBookTable = new ArrayList<>(Arrays.asList(
				isbn, title, publisher, "" + numOfPage, "" + publicationYear, "" + editionNum, description));
		ArrayList<String> expectedKeywordTable = new ArrayList<>(Arrays.asList(
				tag));
		ArrayList<String> expectedPeopleInvolvedTable = new ArrayList<>(Arrays.asList(
				firstName, null, lastName, null));
		
		ArrayList<String> actualBookTable = new ArrayList<>();
		ArrayList<String> actualKeywordTable = new ArrayList<>();
		ArrayList<String> actualPeopleInvolvedTable = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			
			// Book
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + BookTable.TABLE_NAME + " WHERE " + BookTable.TITLE + " = 'TEST123'");
			
			if (rs.next()) {
				actualBookTable.add(rs.getString(BookTable.ISBN));
				actualBookTable.add(rs.getString(BookTable.TITLE));
				actualBookTable.add(rs.getString(BookTable.PUBLISHER));
				actualBookTable.add(rs.getString(BookTable.NUMBER_OF_PAGES));
				actualBookTable.add(rs.getString(BookTable.YEAR_OF_PUBLICATION));
				actualBookTable.add(rs.getString(BookTable.EDITION_NUMBER));
				actualBookTable.add(rs.getString(BookTable.ABSTRACT));
			}
			
			// Keyword
			rs = stmt.executeQuery("SELECT * FROM " + KeywordTable.TABLE_NAME + " WHERE " + KeywordTable.ID + " = 1");
			if (rs.next()) {
				// Tag
				actualKeywordTable.add(rs.getString(KeywordTable.TAG));
			}
			
			// PeopleInvolved
			rs = stmt.executeQuery("SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " WHERE " + PeopleInvolvedTable.ID + " = 1");
			if (rs.next()) {
				// FirstName
				actualPeopleInvolvedTable.add(rs.getString(PeopleInvolvedTable.FIRST_NAME));
				// MiddleName
				actualPeopleInvolvedTable.add(rs.getString(PeopleInvolvedTable.MIDDLE_NAME));
				// FamilyName
				actualPeopleInvolvedTable.add(rs.getString(PeopleInvolvedTable.FAMILY_NAME));
				// Gender
				actualPeopleInvolvedTable.add(rs.getString(PeopleInvolvedTable.GENDER));
			}
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		assertEquals(expectedBookTable, actualBookTable);
		assertEquals(expectedKeywordTable, actualKeywordTable);
		assertEquals(expectedPeopleInvolvedTable, actualPeopleInvolvedTable);
	}

}
