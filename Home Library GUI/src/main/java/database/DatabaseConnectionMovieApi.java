package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.tables.AwardTable;
import database.tables.CrewMemberTable;
import database.tables.MovieTable;
import database.tables.MusicTable;
import database.tables.Role;
import database.tables.RoleTable;
import items.Movie;
import items.Person;

public class DatabaseConnectionMovieApi extends DatabaseConnectionApi {
	
	/****************************************
	 * INSERT MOVIE *
	 ****************************************/
	
	/**
	 * Insert a new Movie
	 * @param movie takes in a movie object containing all the movie info from the front end
	 * @throws SQLException 
	 */
	public static void insertMovie(Movie movie) throws SQLException {
		try {
			
			// insert into Movie table
			insertIntoMovie(movie);
			
			// insert into Crew member table
			insertIntoCrewMember(movie);
			
			// insert into Award table
			insertIntoAward(movie);
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Try to find the ID for the role given in the Role table
	 * @param String role (Use the constant in Role.java)
	 * @return ID of the role iff the role already exists, else return null
	 * @throws SQLException 
	 */
	private static String tryToFindRole(String role) throws SQLException {
		String result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + RoleTable.TABLE_NAME + " "
					+ "WHERE " + RoleTable.DESCRIPTION + " = " + role);
			if (rs.next()) {
				result = rs.getString(RoleTable.ID);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	/**
	 * Add a new role to the Role table
	 * @param role the new role description that is going to be put into the database
	 * @throws SQLException 
	 */
	private static void insertIntoRole(String role) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("INSERT INTO " + RoleTable.TABLE_NAME + " "
					+ "(" + RoleTable.DESCRIPTION + ") "
					+ "VALUES (" + role + ")");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * The purpose of this function is to find or create a role in the database
	 * 	  find iff the role exist, otherwise create the role
	 * @param person the person you are trying to find or create
	 * @return the ID of the person
	 * @throws SQLException 
	 */
	private static String findOrCreateRole(String role) throws SQLException {
		String roleID = null;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			roleID = tryToFindRole(role);
			
			if (roleID == null) {
				insertIntoRole(role);
				roleID = tryToFindRole(role);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return roleID;
	}
	
	/**
	 * Insert Movie data to the Movie table
	 * @param movie a movie object holding all Movie info
	 * @throws SQLException 
	 */
	private static void insertIntoMovie(Movie movie) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("INSERT INTO " + MovieTable.TABLE_NAME + " "
					+ "(" + MovieTable.MOVIE_NAME + ", " + MovieTable.YEAR + ") "
					+ "VALUES (" + movie.getMovieName() + ", " + movie.getReleaseYear() + ")");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Insert Movie crew member into the crew member table
	 * @param movie a object that contains all movie info
	 * @param person the person in the crew
	 * @param role the role of the person
	 * @throws SQLException 
	 */
	private static void insertIntoCrewMemberHelper(Movie movie, Person person, String role) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			// Get the id of the person involved
			String personID = findOrCreatePerson(person);
			// Get the id of the person's role
			String roleID = findOrCreateRole(role);
			
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("INSERT INTO " + CrewMemberTable.TABLE_NAME + " "
					+ "(" + CrewMemberTable.PEOPLE_INVOLVED_ID + ", " + CrewMemberTable.MOVIE_NAME + ", "
					+ CrewMemberTable.RELEASE_YEAR + ", " + CrewMemberTable.ROLE_ID + ") "
					+ "VALUES (" + personID + ", " + movie.getMovieName() + ", "
					+ movie.getReleaseYear() + ", " + roleID + ")");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Insert Movie all crew members into the crew member table
	 * @param movie a object that contains all movie info
	 * @throws SQLException 
	 */
	private static void insertIntoCrewMember(Movie movie) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			// Insert directors
			for (Person director : movie.getDirectorList()) {
				insertIntoCrewMemberHelper(movie, director, Role.DIRECTOR);
			}
			
			// Insert Script Writers
			for (Person scriptWriter : movie.getScriptWriterList()) {
				insertIntoCrewMemberHelper(movie, scriptWriter, Role.SCRIPT_WRITER);
			}
			
			// Insert Casts
			for (Person cast : movie.getCastList()) {
				insertIntoCrewMemberHelper(movie, cast, Role.CAST);
			}
			
			// Insert Producers
			for (Person producer : movie.getProducerList()) {
				insertIntoCrewMemberHelper(movie, producer, Role.PRODUCER);
			}
			
			// Insert Composers
			for (Person composer : movie.getComposerList()) {
				insertIntoCrewMemberHelper(movie, composer, Role.COMPOSER);
			}
			
			// Insert Editors
			for (Person editor : movie.getEditorList()) {
				insertIntoCrewMemberHelper(movie, editor, Role.EDITOR);
			}
			
			// Insert Costume Designer
			for (Person designer : movie.getCostumeDesignerList()) {
				insertIntoCrewMemberHelper(movie, designer, Role.COSTUME_DESIGNER);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Insert movie data in to the award table
	 * @param movie a object that contains movie info
	 * @throws SQLException 
	 */
	private static void insertIntoAward(Movie movie) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			// loop through each cast
			for (Person cast : movie.getCastList()) {
				// Find the cast ID
				String castID = findOrCreatePerson(cast);
				
				Statement stmt = null;
				stmt = connection.createStatement();
				stmt.executeUpdate("INSERT INTO " + AwardTable.TABLE_NAME + " "
						+ "(" + AwardTable.PEOPLE_INVOLVED_ID + ", " + AwardTable.MOVIE_NAME + ", "
						+ AwardTable.YEAR + ", " + AwardTable.AWARD + ") "
						+ "VALUES (" + castID + ", " + movie.getMovieName() + ", "
						+ movie.getReleaseYear() + ", " + movie.getAward() + ")");
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/*************************************
	 * UPDATE MOVIE *
	 *************************************/
	
	/**
	 * Try to find the movie with the given movie Name
	 * @param movieName the title of the album being searched
	 * @return the movieName iff the movie name exists in the database, otherwise return null
	 * @throws SQLException 
	 */
	public static String tryToFindMovieName(String movieName) throws SQLException {
		String result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + MovieTable.TABLE_NAME + " "
					+ "WHERE " + MovieTable.MOVIE_NAME + " = '" + movieName + "'");
			if (rs.next()) {
				result = rs.getString(MovieTable.MOVIE_NAME);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
}
