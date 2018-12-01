package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.sun.org.apache.xpath.internal.compiler.Keywords;

import app.HL_xiewen4;
import database.tables.BookAuthorTable;
import database.tables.BookKeywordTable;
import database.tables.BookTable;
import database.tables.CrewMemberTable;
import database.tables.KeywordTable;
import database.tables.PeopleInvolvedMusicTable;
import database.tables.PeopleInvolvedTable;
import database.tables.RoleTable;
import items.Book;
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
							+ "NATURAL JOIN " + bookAuthorQuery + " "
						+ "ORDER BY " + BookTable.ISBN;
				
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
			person = new Person(newList.get(2), newList.get(0));
			person.setMiddleName(newList.get(1));
		} else if (newList.size() == 2) {
			person = new Person(newList.get(1), newList.get(0));
		}
		
		return person;
	}
	
	/*********************************
	 * Publications in one Year *
	 *********************************/
	
	/**
	 * Find all the books, which published in the same year
	 * @param year
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> generatePublicationInOneYear(int year) {
		HashMap<String, ArrayList<String>> result = null;
		try {
			// Disable auto commit
			disableAutoCommit();
			
			result = generatePublicationInOneYearHelper(year);
			
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
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static HashMap<String, ArrayList<String>> generatePublicationInOneYearHelper(int year) throws SQLException {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		result.put("ISBN", new ArrayList<>());
		result.put("Book’s Name", new ArrayList<>());
		result.put("Published year", new ArrayList<>());
		result.put("Authors’ Family Name", new ArrayList<>());
		result.put("Initial of First Name", new ArrayList<>());
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			String bookQuery = 
					"(SELECT * FROM " + BookTable.TABLE_NAME + " "
					+ "WHERE " + BookTable.YEAR_OF_PUBLICATION + " = ?) AS b";
			String query = 
					"SELECT "
							+ BookTable.ISBN + ", "
							+ BookTable.TITLE + ", "
							+ BookTable.YEAR_OF_PUBLICATION + ", "
							+ PeopleInvolvedTable.FAMILY_NAME + ", "
							+ PeopleInvolvedTable.FIRST_NAME + " "
					+ "FROM " 
							+ bookQuery + " "
							+ "NATURAL JOIN " + BookAuthorTable.TABLE_NAME + " "
							+ "INNER JOIN " + PeopleInvolvedTable.TABLE_NAME + " ON " + BookAuthorTable.AUTHOR_ID + " = " + PeopleInvolvedTable.ID + " "
					+ "ORDER BY "
						+ BookTable.TITLE + ", "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ PeopleInvolvedTable.FIRST_NAME;
					
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, year);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String bookTitle = rs.getString(BookTable.TITLE);
				
				if (!result.get("Book’s Name").contains(bookTitle)) {
					result.get("ISBN").add(rs.getString(BookTable.ISBN));
					result.get("Book’s Name").add(rs.getString(BookTable.TITLE));
					result.get("Published year").add(rs.getString(BookTable.YEAR_OF_PUBLICATION));
					result.get("Authors’ Family Name").add(PeopleInvolvedTable.FAMILY_NAME);
					
					String firstNameInitial = rs.getString(PeopleInvolvedTable.FIRST_NAME).substring(0, 1) + ".";
					result.get("Initial of First Name").add(firstNameInitial);
				}
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	/*****************************
	 * Books With Similar Topic *
	 *****************************/
	
	/**
	 * Find all the books, which is about a specific subject. The subject of a book can be
	 * representing title, description or keywords that define the book.
	 * @param topic
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> generateBooksWithSimilarTopic(String topic) {
		HashMap<String, ArrayList<String>> result = null;
		try {
			// Disable auto commit
			disableAutoCommit();
			
			result = generateBooksWithSimilarTopicHelper(topic);
			
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
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static HashMap<String, ArrayList<String>> generateBooksWithSimilarTopicHelper(String topic) throws SQLException {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		result.put("ISBN", new ArrayList<>());
		result.put("Book’s Name", new ArrayList<>());
		result.put("Published year", new ArrayList<>());
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			String keywordQuery =
					"(SELECT * FROM " + KeywordTable.TABLE_NAME + " "
					+ "WHERE " + KeywordTable.TAG + " LIKE ?) AS k";
			String checkBookTagQuery =
					"(SELECT "
						+ BookTable.ISBN + ", "
						+ BookTable.TITLE + ", "
						+ BookTable.YEAR_OF_PUBLICATION + " "
					+ "FROM "
						+ keywordQuery + " "
						+ "INNER JOIN " 
						+ BookKeywordTable.TABLE_NAME + " ON " + BookKeywordTable.TABLE_NAME + "." + BookKeywordTable.KEYWORD_ID + " = k" + "." + KeywordTable.ID + " "
						+ "NATURAL JOIN " 
						+ BookTable.TABLE_NAME + ") AS cb";
			String bookQuery = 
					"(SELECT "
						+ BookTable.ISBN + ", "
						+ BookTable.TITLE + ", "
						+ BookTable.YEAR_OF_PUBLICATION + " "
					+ "FROM " 
						+ BookTable.TABLE_NAME + " "
					+ "WHERE "
						+ BookTable.TITLE + " LIKE ? OR " 
						+ BookTable.ABSTRACT + " LIKE ?)"; 
			String query = 
					"SELECT DISTINCT "
						+ BookTable.ISBN + ", "
						+ BookTable.TITLE + ", "
						+ BookTable.YEAR_OF_PUBLICATION + " "
					+ "FROM "
						+ checkBookTagQuery + " "
						+ "UNION " 
						+ bookQuery + " "
					+ "ORDER BY " + BookTable.ISBN;
					
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, "%" + topic + "%");
			ps.setString(2, "%" + topic + "%");
			ps.setString(3, "%" + topic + "%");
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String bookTitle = rs.getString(BookTable.TITLE);
				String bookISBN = rs.getString(BookTable.ISBN);
				String bookPublishedYear = rs.getString(BookTable.YEAR_OF_PUBLICATION);
				
				result.get("ISBN").add(bookISBN);
				result.get("Book’s Name").add(bookTitle);
				result.get("Published year").add(bookPublishedYear);
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	/***************************
	 * Frequent Publishers *
	 ***************************/
	
	/**
	 * Find all the authors who published books in at least
	 * two consecutive years.
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> generateFrequentPublishers() {
		HashMap<String, ArrayList<String>> result = null;
		try {
			// Disable auto commit
			disableAutoCommit();
			
			result = generateFrequentPublishersHelper();
			
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
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static HashMap<String, ArrayList<String>> generateFrequentPublishersHelper() throws SQLException {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		result.put("ISBN", new ArrayList<>());
		result.put("Book’s Name", new ArrayList<>());
		result.put("Author’s name", new ArrayList<>());
		result.put("Published year", new ArrayList<>());
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			String authorNameQuery =
					"(SELECT * "
					+ "FROM "
						+ BookAuthorTable.TABLE_NAME + " AS ba "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " AS pi "
								+ "ON ba." + BookAuthorTable.AUTHOR_ID + " = pi." + PeopleInvolvedTable.ID + ") AS bapi";
			
			String authorAndBook =
					"SELECT * "
					+ "FROM "
						+ authorNameQuery + " "
						+ "NATURAL JOIN "
						+ BookTable.TABLE_NAME + " ";
			
			String twoConsecutiveYearQuery =
					"(SELECT t1." + PeopleInvolvedTable.ID + " "
							+ "FROM "
								+ "(" + authorAndBook + ") AS t1 "
								+ "INNER JOIN "
								+ "(" + authorAndBook + ") AS t2 "
									+ "ON t1." + BookTable.YEAR_OF_PUBLICATION + " = t2." + BookTable.YEAR_OF_PUBLICATION + "+1 "
										+ "and t1." + BookAuthorTable.AUTHOR_ID + " = t2." + BookAuthorTable.AUTHOR_ID + ")";
			
			String query = 
					"SELECT "
						+ BookTable.YEAR_OF_PUBLICATION + ", "
						+ BookTable.ISBN + ", "
						+ BookTable.TITLE + ", "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ PeopleInvolvedTable.FIRST_NAME + ", "
						+ PeopleInvolvedTable.MIDDLE_NAME + " "
					+ "FROM "
						+ "(" + authorAndBook + ") AS author "
					+ "WHERE " 
						+ PeopleInvolvedTable.ID + " IN " + twoConsecutiveYearQuery + " "
					+ "ORDER BY "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ PeopleInvolvedTable.FIRST_NAME + ", "
						+ BookTable.YEAR_OF_PUBLICATION;
					
			
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String bookTitle = rs.getString(BookTable.TITLE);
				String bookISBN = rs.getString(BookTable.ISBN);
				String bookPublishedYear = rs.getString(BookTable.YEAR_OF_PUBLICATION);
				String authorFN = rs.getString(PeopleInvolvedTable.FIRST_NAME);
				String authorMN = rs.getString(PeopleInvolvedTable.MIDDLE_NAME);
				String authorLN = rs.getString(PeopleInvolvedTable.FAMILY_NAME);
				
				result.get("ISBN").add(bookISBN);
				result.get("Book’s Name").add(bookTitle);
				result.get("Published year").add(bookPublishedYear);
				
				String authorName = authorFN;
				if (authorMN != null)
					authorName = authorName + " " + authorMN;
				authorName = authorName + " " + authorLN;
				result.get("Author’s name").add(authorName);
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	/**************************
	 * Most Popular Subject *
	 **************************/
	
	/**
	 * Find the most popular subject in your library, which is the
	 * subject for which a similar tag was used more than once.
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> generateMostPopularSubject() {
		HashMap<String, ArrayList<String>> result = null;
		try {
			// Disable auto commit
			disableAutoCommit();
			
			result = generateMostPopularSubjectHelper();
			
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
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static HashMap<String, ArrayList<String>> generateMostPopularSubjectHelper() throws SQLException {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		result.put("Tag", new ArrayList<>());
		result.put("Frequency", new ArrayList<>());
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			String bookTagQuery = 
					"(SELECT * "
					+ "FROM "
						+ BookKeywordTable.TABLE_NAME + " AS bk "
						+ "INNER JOIN "
						+ KeywordTable.TABLE_NAME + " AS k "
							+ "ON bk." + BookKeywordTable.KEYWORD_ID + " = k." + KeywordTable.ID + ") AS bt";
			
			String query = 
					"SELECT "
						+ KeywordTable.TAG + ", "
						+ "COUNT(" + BookKeywordTable.ISBN + ") AS Frequency "
					+ "FROM "
						+ bookTagQuery + " "
					+ "GROUP BY "
						+ "bt." + BookKeywordTable.KEYWORD_ID + " "
					+ "ORDER BY "
						+ "Frequency, "
						+ KeywordTable.TAG + " "
					+ "LIMIT 1";
					
			
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				result.get("Tag").add(rs.getString(KeywordTable.TAG));
				result.get("Frequency").add(rs.getString("Frequency"));
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	/***************************
	 * Multi Skills Movie Crew *
	 ***************************/
	
	/**
	 * Find all people who played more than one role in producing a movie.
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> generateMultiSkillsMovieCrew() {
		HashMap<String, ArrayList<String>> result = null;
		try {
			// Disable auto commit
			disableAutoCommit();
			
			result = generateMultiSkillsMovieCrewHelper();
			
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
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static HashMap<String, ArrayList<String>> generateMultiSkillsMovieCrewHelper() throws SQLException {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		result.put("Family Name", new ArrayList<>());
		result.put("Role", new ArrayList<>());
		result.put("Movie Name", new ArrayList<>());
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String memberWithMoreThan2Role =
					"(SELECT "
						+ CrewMemberTable.PEOPLE_INVOLVED_ID + ", "
						+ CrewMemberTable.MOVIE_NAME + ", "
						+ "COUNT(" + CrewMemberTable.ROLE_ID + ") AS RoleNum "
					+ "FROM "
						+ CrewMemberTable.TABLE_NAME + " "
					+ "GROUP BY "
						+ CrewMemberTable.PEOPLE_INVOLVED_ID + ", "
						+ CrewMemberTable.MOVIE_NAME + ", "
						+ CrewMemberTable.RELEASE_YEAR + " "
					+ "HAVING RoleNum > 1) AS mt2r"; // more than two roles
			
			String descriptionQuery = 
					"(SELECT * "
					+ "FROM "
						+ CrewMemberTable.TABLE_NAME + " AS cm "
						+ "INNER JOIN "
						+ RoleTable.TABLE_NAME + " AS r ON r." + RoleTable.ID + " = cm." + CrewMemberTable.ROLE_ID + ") AS cmr";
			String query = 
					"SELECT "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ RoleTable.DESCRIPTION + ", "
						+ CrewMemberTable.MOVIE_NAME + " "
					+ "FROM "
						+ memberWithMoreThan2Role + " "
						+ "NATURAL JOIN "
						+ descriptionQuery + " "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " AS pi "
								+ "ON mt2r." + CrewMemberTable.PEOPLE_INVOLVED_ID + " = pi." + PeopleInvolvedTable.ID + " "
					+ "ORDER BY "
						+ PeopleInvolvedTable.FAMILY_NAME;
					
			
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String movieName = rs.getString(CrewMemberTable.MOVIE_NAME);
				String role = rs.getString(RoleTable.DESCRIPTION);
				String familyName = rs.getString(PeopleInvolvedTable.FAMILY_NAME);
				
				result.get("Family Name").add(familyName);
				result.get("Role").add(role);
				result.get("Movie Name").add(movieName);
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
}
