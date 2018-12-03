package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.sun.org.apache.xpath.internal.compiler.Keywords;

import app.HL_xiewen4;
import database.tables.AwardTable;
import database.tables.BookAuthorTable;
import database.tables.BookKeywordTable;
import database.tables.BookTable;
import database.tables.CrewMemberTable;
import database.tables.KeywordTable;
import database.tables.MusicSingerTable;
import database.tables.MusicTable;
import database.tables.PeopleInvolvedMusicTable;
import database.tables.PeopleInvolvedTable;
import database.tables.Role;
import database.tables.RoleTable;
import items.Book;
import items.Person;

/**
 * @author xiewen4
 * This api is use to generate reports
 */
public class DatabaseConnectionReportApi extends DatabaseConnectionApi {
	
	// Helper
	
	/**
	 * Create a view
	 * @throws SQLException 
	 */
	private static String createView(String query) throws SQLException {
		String viewName = null;
		try {
			Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword);
			viewName = "View" + (int)(Math.floor(Math.random() * 5000));
			
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("CREATE VIEW " + viewName + " AS "
					+ query);
			
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		
		return viewName;
	}
	
	
	private static void dropView(String viewName) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("DROP VIEW " + viewName);
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
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
		
		Integer authorID = null;
		if (author != null)
			authorID = tryToFindPerson(author);
		
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
			
			String authorAndBookView = createView(authorAndBook);
			
			String twoConsecutiveYearQuery =
					"(SELECT t1." + PeopleInvolvedTable.ID + " "
							+ "FROM "
								+ authorAndBookView + " AS t1 "
								+ "INNER JOIN "
								+ authorAndBookView + " AS t2 "
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
			
			dropView(authorAndBookView);
			
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
			
			String tagCountQuery = 
					"SELECT "
						+ KeywordTable.TAG + ", "
						+ "COUNT(" + BookKeywordTable.ISBN + ") AS Frequency "
					+ "FROM "
						+ bookTagQuery + " "
					+ "GROUP BY "
						+ "bt." + BookKeywordTable.KEYWORD_ID;
			
			String viewName = createView(tagCountQuery);
			
			String maxCount =
					"(SELECT MAX(Frequency) FROM " + viewName + ")";
			
			String query = 
					"SELECT * "
					+ "FROM "
						+ viewName + " "
					+ "WHERE "
						+ "Frequency = " + maxCount + " "
					+ "ORDER BY "
						+ KeywordTable.TAG;
					
			
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				result.get("Tag").add(rs.getString(KeywordTable.TAG));
				result.get("Frequency").add(rs.getString("Frequency"));
			}
			
			dropView(viewName);
			
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
	
	
	/*************************
	 * Award Winning Movies *
	 *************************/
	
	/**
	 * Find all the movies directed by the same person and
	 * received at least one award.
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> generateAwardWinningMovies() {
		HashMap<String, ArrayList<String>> result = null;
		try {
			// Disable auto commit
			disableAutoCommit();
			
			result = generateAwardWinningMoviesHelper();
			
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
	
	private static HashMap<String, ArrayList<String>> generateAwardWinningMoviesHelper() throws SQLException {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		result.put("Movie Name", new ArrayList<>());
		result.put("Director Name", new ArrayList<>());
		result.put("# of Awards", new ArrayList<>());
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String directorQuery =
					"(SELECT * "
					+ "FROM "
						+ CrewMemberTable.TABLE_NAME + " AS cm "
						+ "NATURAL JOIN "
						+ "(SELECT " + RoleTable.DESCRIPTION + ", " + RoleTable.ID + " AS " + CrewMemberTable.ROLE_ID + " FROM " + RoleTable.TABLE_NAME + " WHERE " + RoleTable.DESCRIPTION + " = ?) AS dr "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " AS pi "
								+ "ON cm." + CrewMemberTable.PEOPLE_INVOLVED_ID + " = pi." + PeopleInvolvedTable.ID + ") AS d";
			
			String awardQuery = 
					"(SELECT "
						+ AwardTable.MOVIE_NAME + ", "
						+ "COUNT(" + AwardTable.AWARD + ") AS awardCount "
					+ "FROM " 
						+ AwardTable.TABLE_NAME + " "
					+ "WHERE " 
						+ AwardTable.AWARD + " IS NOT NULL "
						+ "and " + AwardTable.AWARD + " > 0 "
					+ "GROUP BY "
						+ AwardTable.MOVIE_NAME + ") AS a";
			
			String query = 
					"SELECT "
						+ CrewMemberTable.MOVIE_NAME + ", "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ PeopleInvolvedTable.FIRST_NAME + ", "
						+ PeopleInvolvedTable.MIDDLE_NAME + ", "
						+ "awardCount "
					+ "FROM "
						+ awardQuery + " "
						+ "NATURAL JOIN "
						+ directorQuery + " "
					+ "ORDER BY "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ PeopleInvolvedTable.FIRST_NAME + ", "
						+ CrewMemberTable.MOVIE_NAME;
					
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, Role.DIRECTOR);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String movieName = rs.getString(CrewMemberTable.MOVIE_NAME);
				String directorName = rs.getString(PeopleInvolvedTable.FIRST_NAME);
				if (rs.getString(PeopleInvolvedTable.MIDDLE_NAME) != null)
					directorName = directorName + " " + PeopleInvolvedTable.FAMILY_NAME;
				directorName = directorName + " " + rs.getString(PeopleInvolvedTable.FAMILY_NAME);
				String numOfAward = rs.getString("awardCount");
				
				result.get("Movie Name").add(movieName);
				result.get("Director Name").add(directorName);
				result.get("# of Awards").add(numOfAward);
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	/****************************
	 * Music With Similar Name *
	 ****************************/
	
	/**
	 * Find the singers of all the music, which share the same name.
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> generateMusicWithSimilarName() {
		HashMap<String, ArrayList<String>> result = null;
		try {
			// Disable auto commit
			disableAutoCommit();
			
			result = generateMusicWithSimilarNameHelper();
			
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
	
	private static HashMap<String, ArrayList<String>> generateMusicWithSimilarNameHelper() throws SQLException {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		result.put("Album Name", new ArrayList<>());
		result.put("Music Name", new ArrayList<>());
		result.put("Singers", new ArrayList<>());
		result.put("Year", new ArrayList<>());
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String musicCountQuery =
					"(SELECT "
						+ MusicSingerTable.MUSIC_NAME + ", "
						+ "COUNT(" + MusicSingerTable.MUSIC_NAME + ") AS musicCount "
					+ "FROM "
						+ MusicSingerTable.TABLE_NAME + " "
					+ "GROUP BY "
						+ MusicSingerTable.MUSIC_NAME + ") AS tm";
			
			String uniqueSingersQuery = 
					"(SELECT "
						+ MusicSingerTable.MUSIC_NAME + ", "
						+ MusicSingerTable.PEOPLE_INVOLVED_ID + " "
					+ "FROM "
						+ MusicSingerTable.TABLE_NAME + " "
					+ "GROUP BY "
						+ MusicSingerTable.MUSIC_NAME + ", "
						+ MusicSingerTable.PEOPLE_INVOLVED_ID + ") AS us";
			
			String moreThan2CountUniqueSingerQuery = 
					"(SELECT * "
					+ "FROM "
						+ musicCountQuery + " "
						+ "NATURAL JOIN "
						+ MusicSingerTable.TABLE_NAME + " "
						+ "NATURAL JOIN "
						+ uniqueSingersQuery + " "
					+ "WHERE "
						+ "musicCount > 1) AS mt2cus";
			
			String query =
					"SELECT * "
					+ "FROM "
						+ moreThan2CountUniqueSingerQuery + " "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " AS pi "
								+ "ON pi." + PeopleInvolvedTable.ID + " = mt2cus." + MusicSingerTable.PEOPLE_INVOLVED_ID + " "
					+ "ORDER BY "
						+ MusicSingerTable.MUSIC_NAME + ", "
						+ MusicSingerTable.YEAR;

					
			
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String albumName = rs.getString(MusicSingerTable.ALBUM_NAME);
				String musicName = rs.getString(MusicSingerTable.MUSIC_NAME);
				String singerFN = rs.getString(PeopleInvolvedTable.FIRST_NAME);
				String singerSN = rs.getString(PeopleInvolvedTable.FAMILY_NAME);
				String singerMN = rs.getString(PeopleInvolvedTable.MIDDLE_NAME);
				String year = rs.getString(MusicSingerTable.YEAR);
				
				String singerName = singerFN;
				if (singerMN != null)
					singerName = singerName + " " + singerMN;
				singerName = singerName + " " + singerSN;
				
				result.get("Album Name").add(albumName);
				result.get("Music Name").add(musicName);
				result.get("Singers").add(singerName);
				result.get("Year").add(year);
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	/***************************
	 * Multi Skill Music Crew *
	 ***************************/
	
	/**
	 * Find all the people who are both the composer and song
	 * writer for a music but not are not an arranger.
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> generateMultiSkillMusicCrew() {
		HashMap<String, ArrayList<String>> result = null;
		try {
			// Disable auto commit
			disableAutoCommit();
			
			result = generateMultiSkillMusicCrewHelper();
			
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
	
	private static HashMap<String, ArrayList<String>> generateMultiSkillMusicCrewHelper() throws SQLException {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		result.put("Family Name", new ArrayList<>());
		result.put("First Name Initial", new ArrayList<>());
		result.put("Music Name", new ArrayList<>());
		result.put("Album Name", new ArrayList<>());
		result.put("Year", new ArrayList<>());
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String isSWAndCQuery =
					"(SELECT *"
					+ "FROM "
						+ PeopleInvolvedMusicTable.TABLE_NAME + " "
					+ "WHERE "
						+ PeopleInvolvedMusicTable.IS_SONG_WRITER + " IS NOT NULL "
						+ "and " + PeopleInvolvedMusicTable.IS_SONG_WRITER + " > 0 "
						+ "and " + PeopleInvolvedMusicTable.IS_COMPOSER + " IS NOT NULL "
						+ "and " + PeopleInvolvedMusicTable.IS_COMPOSER + " > 0) AS isswc";
			
			String isSWCNotAQuery = 
					"(SELECT * "
					+ "FROM "
						+ isSWAndCQuery + " "
					+ "WHERE "
						+ PeopleInvolvedMusicTable.IS_ARRANGER + " IS NULL "
						+ "OR " + PeopleInvolvedMusicTable.IS_ARRANGER + " = 0) AS isSWCNotA";
			
			String query = 
					"SELECT * "
					+ "FROM "
						+ isSWCNotAQuery + " "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " AS pi "
								+ "ON isSWCNotA." + PeopleInvolvedMusicTable.PEOPLE_INVOLVED_ID + " = pi." + PeopleInvolvedTable.ID + " "
					+ "ORDER BY "
						+ PeopleInvolvedMusicTable.YEAR + ", "
						+ PeopleInvolvedMusicTable.MUSIC_NAME + " DESC";
					
			
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String familyName = rs.getString(PeopleInvolvedTable.FAMILY_NAME);
				String firstNameInitial = rs.getString(PeopleInvolvedTable.FIRST_NAME).substring(0, 1) + ".";
				String musicName = rs.getString(PeopleInvolvedMusicTable.MUSIC_NAME);
				String albumName = rs.getString(PeopleInvolvedMusicTable.ALBUM_NAME);
				String year = rs.getString(PeopleInvolvedMusicTable.YEAR);
				
				result.get("Family Name").add(familyName);
				result.get("First Name Initial").add(firstNameInitial);
				result.get("Music Name").add(musicName);
				result.get("Album Name").add(albumName);
				result.get("Year").add(year);
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	/*********************
	 * Similar Names *
	 *********************/
	
	/**
	 * Find the similar family names that have been an author or
	 * part of music or movie production. It is possible that --
	 * they are not the same person
	 * and only share the same family name.
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> generateSimilarNames() {
		HashMap<String, ArrayList<String>> result = null;
		try {
			// Disable auto commit
			disableAutoCommit();
			
			result = generateSimilarNamesHelper();
			
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
	
	private static HashMap<String, ArrayList<String>> generateSimilarNamesHelper() throws SQLException {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		result.put("Family Name", new ArrayList<>());
		result.put("Roles", new ArrayList<>());
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			// author
			String authorQuery =
					"(SELECT "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ "? AS Roles "
					+ "FROM "
						+ BookAuthorTable.TABLE_NAME + " "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " "
								+ "ON " + PeopleInvolvedTable.ID + " = " + BookAuthorTable.AUTHOR_ID + " "
					+ "GROUP BY "
						+ PeopleInvolvedTable.FAMILY_NAME + ") AS authorQ";
			
			// "Singer";
			String singerQuery =
					"(SELECT "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ "? AS Roles "
					+ "FROM "
						+ MusicSingerTable.TABLE_NAME + " "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " "
								+ "ON " + MusicSingerTable.PEOPLE_INVOLVED_ID + " = " + PeopleInvolvedTable.ID + " "
					+ "GROUP BY "
						+ PeopleInvolvedTable.FAMILY_NAME + ")";
			
			// "Producer";
			String producerQuery =
					"(SELECT "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ "? AS Roles "
					+ "FROM "
						+ MusicTable.TABLE_NAME + " "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " "
								+ "ON " + MusicTable.PRODUCER_ID + " = " + PeopleInvolvedTable.ID + " "
					+ "GROUP BY "
						+ PeopleInvolvedTable.FAMILY_NAME + ")";
			
			// "Song Writer";
			String songWriterQuery =
					"(SELECT "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ "? AS Roles "
					+ "FROM "
						+ PeopleInvolvedMusicTable.TABLE_NAME + " "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " "
								+ "ON " + PeopleInvolvedMusicTable.PEOPLE_INVOLVED_ID + " = " + PeopleInvolvedTable.ID + " "
					+ "WHERE "
						+ PeopleInvolvedMusicTable.IS_SONG_WRITER + " IS NOT NULL "
						+ "and " + PeopleInvolvedMusicTable.IS_SONG_WRITER + " > 0 "
					+ "GROUP BY "
						+ PeopleInvolvedTable.FAMILY_NAME + ")";
			
			// "Composer";
			String composerQuery =
					"(SELECT "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ "? AS Roles "
					+ "FROM "
						+ PeopleInvolvedMusicTable.TABLE_NAME + " "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " "
								+ "ON " + PeopleInvolvedMusicTable.PEOPLE_INVOLVED_ID + " = " + PeopleInvolvedTable.ID + " "
					+ "WHERE "
						+ PeopleInvolvedMusicTable.IS_COMPOSER + " IS NOT NULL "
						+ "and " + PeopleInvolvedMusicTable.IS_COMPOSER + " > 0 "
					+ "GROUP BY "
						+ PeopleInvolvedTable.FAMILY_NAME + ")";
			
			// "Arranger";
			String arrangerQuery =
					"(SELECT "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ "? AS Roles "
					+ "FROM "
						+ PeopleInvolvedMusicTable.TABLE_NAME + " "
						+ "INNER JOIN "
						+ PeopleInvolvedTable.TABLE_NAME + " "
								+ "ON " + PeopleInvolvedMusicTable.PEOPLE_INVOLVED_ID + " = " + PeopleInvolvedTable.ID + " "
					+ "WHERE "
						+ PeopleInvolvedMusicTable.IS_ARRANGER + " IS NOT NULL "
						+ "and " + PeopleInvolvedMusicTable.IS_ARRANGER + " > 0 "
					+ "GROUP BY "
						+ PeopleInvolvedTable.FAMILY_NAME + ")";
			
			// "Director";
			// "Script Writer";
			// "Cast";
			// "Editor";
			// "Costume Designer";
			String movieQuery = 
					"(SELECT "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ RoleTable.DESCRIPTION + " AS Roles "
					+ "FROM "
						+ PeopleInvolvedTable.TABLE_NAME + " "
						+ "INNER JOIN "
						+ CrewMemberTable.TABLE_NAME + " "
								+ "ON " + CrewMemberTable.PEOPLE_INVOLVED_ID + " = " + PeopleInvolvedTable.ID + " "
						+ "NATURAL JOIN "
						+ "(SELECT " + RoleTable.ID + " AS " + CrewMemberTable.ROLE_ID + ", " + RoleTable.DESCRIPTION + " FROM " + RoleTable.TABLE_NAME + ") AS role "
					+ "GROUP BY "
						+ PeopleInvolvedTable.FAMILY_NAME + ", "
						+ RoleTable.DESCRIPTION + ")";
			
			String query = 
					"SELECT * "
					+ "FROM "
						+ authorQuery + " "
						+ "UNION "
						+ singerQuery + " "
						+ "UNION "
						+ producerQuery + " "
						+ "UNION "
						+ songWriterQuery + " "
						+ "UNION "
						+ composerQuery + " "
						+ "UNION "
						+ arrangerQuery + " "
						+ "UNION "
						+ movieQuery + " "
					+ "ORDER BY "
						+ PeopleInvolvedTable.FAMILY_NAME;
					
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, Role.AUTHOR);
			ps.setString(2, Role.SINGER);
			ps.setString(3, Role.PRODUCER);
			ps.setString(4, Role.SONG_WRITER);
			ps.setString(5, Role.COMPOSER);
			ps.setString(6, Role.ARRANGER);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String familyName = rs.getString(PeopleInvolvedTable.FAMILY_NAME);
				String role = rs.getString("Roles");
				
				result.get("Family Name").add(familyName);
				result.get("Roles").add(role);
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
}
