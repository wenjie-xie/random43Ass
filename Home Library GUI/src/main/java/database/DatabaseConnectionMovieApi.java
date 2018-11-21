package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
			System.out.println("SELECT * FROM " + RoleTable.TABLE_NAME + " "
					+ "WHERE " + RoleTable.DESCRIPTION + " = " + role);
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + RoleTable.TABLE_NAME + " "
					+ "WHERE " + RoleTable.DESCRIPTION + " = " + role);
			if (rs.next()) {
				result = rs.getString(RoleTable.ID);
			}
			
			connection.close();
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
			System.out.println("INSERT INTO " + RoleTable.TABLE_NAME + " "
					+ "(" + RoleTable.DESCRIPTION + ") "
					+ "VALUES (" + role + ")");
			stmt.executeUpdate("INSERT INTO " + RoleTable.TABLE_NAME + " "
					+ "(" + RoleTable.DESCRIPTION + ") "
					+ "VALUES (" + role + ")");
			
			connection.close();
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
		try {
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
			System.out.println("INSERT INTO " + MovieTable.TABLE_NAME + " "
					+ "(" + MovieTable.MOVIE_NAME + ", " + MovieTable.YEAR + ") "
					+ "VALUES (" + movie.getMovieName() + ", " + movie.getReleaseYear() + ")");
			stmt.executeUpdate("INSERT INTO " + MovieTable.TABLE_NAME + " "
					+ "(" + MovieTable.MOVIE_NAME + ", " + MovieTable.YEAR + ") "
					+ "VALUES (" + movie.getMovieName() + ", " + movie.getReleaseYear() + ")");
			
			connection.close();
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
			System.out.println("INSERT INTO " + CrewMemberTable.TABLE_NAME + " "
					+ "(" + CrewMemberTable.PEOPLE_INVOLVED_ID + ", " + CrewMemberTable.MOVIE_NAME + ", "
					+ CrewMemberTable.RELEASE_YEAR + ", " + CrewMemberTable.ROLE_ID + ") "
					+ "VALUES (" + personID + ", " + movie.getMovieName() + ", "
					+ movie.getReleaseYear() + ", " + roleID + ")");
			stmt.executeUpdate("INSERT INTO " + CrewMemberTable.TABLE_NAME + " "
					+ "(" + CrewMemberTable.PEOPLE_INVOLVED_ID + ", " + CrewMemberTable.MOVIE_NAME + ", "
					+ CrewMemberTable.RELEASE_YEAR + ", " + CrewMemberTable.ROLE_ID + ") "
					+ "VALUES (" + personID + ", " + movie.getMovieName() + ", "
					+ movie.getReleaseYear() + ", " + roleID + ")");
			
			connection.close();
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
		try {
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
				System.out.println("INSERT INTO " + AwardTable.TABLE_NAME + " "
						+ "(" + AwardTable.PEOPLE_INVOLVED_ID + ", " + AwardTable.MOVIE_NAME + ", "
						+ AwardTable.YEAR + ", " + AwardTable.AWARD + ") "
						+ "VALUES (" + castID + ", " + movie.getMovieName() + ", "
						+ movie.getReleaseYear() + ", " + movie.getAward() + ")");
				stmt.executeUpdate("INSERT INTO " + AwardTable.TABLE_NAME + " "
						+ "(" + AwardTable.PEOPLE_INVOLVED_ID + ", " + AwardTable.MOVIE_NAME + ", "
						+ AwardTable.YEAR + ", " + AwardTable.AWARD + ") "
						+ "VALUES (" + castID + ", " + movie.getMovieName() + ", "
						+ movie.getReleaseYear() + ", " + movie.getAward() + ")");
			}
			
			connection.close();
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
			System.out.println("SELECT * FROM " + MovieTable.TABLE_NAME + " "
					+ "WHERE " + MovieTable.MOVIE_NAME + " = '" + movieName + "'");
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + MovieTable.TABLE_NAME + " "
					+ "WHERE " + MovieTable.MOVIE_NAME + " = '" + movieName + "'");
			if (rs.next()) {
				result = rs.getString(MovieTable.MOVIE_NAME);
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	/***********************************
	 * GET MOVIE *
	 ***********************************/
	
	/**
	 * Get the info of a given movieName
	 * @param movieName the name of a movie
	 * @return a movie object containing all the info
	 * @throws SQLException 
	 */
	public static Movie getMovieInfo(String movieName) throws SQLException {
		Movie movie;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + MovieTable.TABLE_NAME + " "
					+ "WHERE " + MovieTable.MOVIE_NAME + " = '" + movieName + "'");
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + MovieTable.TABLE_NAME + " "
					+ "WHERE " + MovieTable.MOVIE_NAME + " = '" + movieName + "'");
			
			int year = Integer.parseInt(rs.getString(MovieTable.YEAR));
			
			movie = new Movie(movieName, year);
			
			// Crew member
			HashMap<String, ArrayList<Person>> crewMemberMap = getCrewMemberMap(movie);
			// Director
			movie.setDirectorList(crewMemberMap.get(Role.DIRECTOR));
			// Script Writer
			movie.setScriptWriterList(crewMemberMap.get(Role.SCRIPT_WRITER));
			// Cast
			movie.setCastList(crewMemberMap.get(Role.CAST));
			// Producer
			movie.setProducerList(crewMemberMap.get(Role.PRODUCER));
			// Composer
			movie.setComposerList(crewMemberMap.get(Role.COMPOSER));
			// Editor
			movie.setEditorList(crewMemberMap.get(Role.EDITOR));
			// Costume Designer
			movie.setCostumeDesignerList(crewMemberMap.get(Role.COSTUME_DESIGNER));
			
			// Award
			movie.setAward(getAward(movie));
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return movie;
	}
	
	
	/**
	 * Get the Description of a given role id
	 * @param id the id of a Role
	 * @return a description of the role (Role class)
	 * @throws SQLException 
	 */
	private static String getRoleDescription(int id) throws SQLException {
		String result;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT " + RoleTable.DESCRIPTION + " FROM " + RoleTable.TABLE_NAME + " "
					+ "WHERE " + RoleTable.ID + " = " + id);
			ResultSet rs = stmt.executeQuery("SELECT " + RoleTable.DESCRIPTION + " FROM " + RoleTable.TABLE_NAME + " "
					+ "WHERE " + RoleTable.ID + " = " + id);
			
			result = rs.getString(RoleTable.DESCRIPTION);
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	/**
	 * Get every crew member's person id and role id
	 * 
	 * @param movie a movie object containing movie info
	 * @return a map that contains two list {("personID", [personID]), ("roleID", [roleID])}
	 * @throws SQLException 
	 */
	private static HashMap<String, ArrayList<Integer>> getPersonIDRoleIDLists(Movie movie) throws SQLException {
		HashMap<String, ArrayList<Integer>> crewMemberMap = new HashMap<>();
		ArrayList<Integer> personIDList = new ArrayList<>();
		ArrayList<Integer> roleIDList = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + CrewMemberTable.TABLE_NAME + " "
					+ "WHERE " + CrewMemberTable.MOVIE_NAME + " = " + movie.getMovieName() + " "
					+ "and " + CrewMemberTable.RELEASE_YEAR + " = " + movie.getReleaseYear());
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + CrewMemberTable.TABLE_NAME + " "
					+ "WHERE " + CrewMemberTable.MOVIE_NAME + " = " + movie.getMovieName() + " "
					+ "and " + CrewMemberTable.RELEASE_YEAR + " = " + movie.getReleaseYear());
			
			while (rs.next()) {
				int roleID = Integer.parseInt(rs.getString(CrewMemberTable.ROLE_ID));
				roleIDList.add(roleID);
				
				int personID = Integer.parseInt(rs.getString(CrewMemberTable.PEOPLE_INVOLVED_ID));
				personIDList.add(personID);
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		crewMemberMap.put("personID", personIDList);
		crewMemberMap.put("roleID", roleIDList);
		
		return crewMemberMap;
	}
	
	
	/**
	 * Put every person that is in the movie crew into a map and return them
	 * 
	 * @param movie a movie object containing movie info
	 * @return a map that contains list of all crew 
	 * @throws SQLException 
	 */
	private static HashMap<String, ArrayList<Person>> getCrewMemberMap(Movie movie) throws SQLException {
		HashMap<String, ArrayList<Person>> crewMemberMap = new HashMap<>();
		ArrayList<Person> directorList = new ArrayList<>();
		ArrayList<Person> scriptWriterList = new ArrayList<>();
		ArrayList<Person> castList = new ArrayList<>();
		ArrayList<Person> producerList = new ArrayList<>();
		ArrayList<Person> composerList = new ArrayList<>();
		ArrayList<Person> editorList = new ArrayList<>();
		ArrayList<Person> costumeDesignerList = new ArrayList<>();
		
		try {
			
			ArrayList<Integer> personIDList = new ArrayList<>();
			ArrayList<Integer> roleIDList = new ArrayList<>();
			
			for (int i = 0; i < personIDList.size(); i++) {
				int roleID = roleIDList.get(i);
				String role = getRoleDescription(roleID);
				
				int personID = personIDList.get(i);
				Person person = getPersonFromPeopleInvolvedTable(personID);
				
				// check the role of the person
				// Director
				if (role.equals(Role.DIRECTOR)) {
					directorList.add(person);
				// Script Writer
				} else if (role.equals(Role.SCRIPT_WRITER)) {
					scriptWriterList.add(person);
				// Cast
				} else if (role.equals(Role.CAST)) {
					castList.add(person);
				// Producer
				} else if (role.equals(Role.PRODUCER)) {
					producerList.add(person);
				// Composer
				} else if (role.equals(Role.COMPOSER)) {
					composerList.add(person);
				// Editor
				} else if (role.equals(Role.EDITOR)) {
					editorList.add(person);
				// Costumer Designer
				} else if (role.equals(Role.COSTUME_DESIGNER)) {
					costumeDesignerList.add(person);
				} else {
					throw new SQLException();
				}
			}
			
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		// Add to map
		crewMemberMap.put(Role.DIRECTOR, directorList);
		crewMemberMap.put(Role.SCRIPT_WRITER, directorList);
		crewMemberMap.put(Role.CAST, castList);
		crewMemberMap.put(Role.PRODUCER, producerList);
		crewMemberMap.put(Role.COMPOSER, composerList);
		crewMemberMap.put(Role.EDITOR, editorList);
		crewMemberMap.put(Role.COSTUME_DESIGNER, costumeDesignerList);
		
		return crewMemberMap;
	}
	
	/**
	 * Get award of the movie
	 * @param movie
	 * @return the award
	 * @throws SQLException 
	 */
	private static int getAward(Movie movie) throws SQLException {
		int result;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT " + AwardTable.AWARD + " FROM " + AwardTable.TABLE_NAME + " "
					+ "WHERE " + AwardTable.MOVIE_NAME + " = " + movie.getMovieName() + " "
					+ "and " + AwardTable.YEAR + " = " + movie.getReleaseYear());
			ResultSet rs = stmt.executeQuery("SELECT " + AwardTable.AWARD + " FROM " + AwardTable.TABLE_NAME + " "
					+ "WHERE " + AwardTable.MOVIE_NAME + " = " + movie.getMovieName() + " "
					+ "and " + AwardTable.YEAR + " = " + movie.getReleaseYear());
			
			result = Integer.parseInt(rs.getString(AwardTable.AWARD));
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	/*********************
	 * UPDATE MOVIE *
	 *********************/
	
	/**
	 * Compare and update the old movie info in the data base into the new movie info given
	 * @param oldMovieInfo
	 * @param newMovieInfo
	 * @throws SQLException 
	 */
	public static void compareAndUpdateMovie(Movie oldMovieInfo, Movie newMovieInfo) throws SQLException {
		// remove the everything from the old movie
		removeMovie(oldMovieInfo);
		
		// insert the new movie
		insertMovie(newMovieInfo);
	}
	
	/*****************************
	 * REMOVE MOVIE *
	 *****************************/
	
	/**
	 * Remove the movie given completely from the database
	 * @param movie
	 * @throws SQLException 
	 */
	public static void removeMovie(Movie movie) throws SQLException {
		// remove movie from Movie table
		removeMovieFromMovieTable(movie);
		
		// remove movie from crew member table
		removeMovieFromCrewMemberTable(movie);
		
		// remove movie from award table
		removeMovieFromAwardTable(movie);
	}
	
	
	/**
	 * Remove movie from Movie Table
	 * @throws SQLException 
	 */
	private static void removeMovieFromMovieTable(Movie movie) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeQuery("DELETE FROM " + MovieTable.TABLE_NAME + " "
					+ "WHERE " + MovieTable.MOVIE_NAME + " = " + movie.getMovieName() + " "
					+ "and " + MovieTable.YEAR + " = " + movie.getReleaseYear());
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Remove movie from Crew Member Table
	 * @throws SQLException 
	 */
	private static void removeMovieFromCrewMemberTable(Movie movie) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeQuery("DELETE FROM " + CrewMemberTable.TABLE_NAME + " "
					+ "WHERE " + CrewMemberTable.MOVIE_NAME + " = " + movie.getMovieName() + " "
					+ "and " + CrewMemberTable.RELEASE_YEAR + " = " + movie.getReleaseYear());
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Remove movie from Award Table
	 * @throws SQLException 
	 */
	private static void removeMovieFromAwardTable(Movie movie) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeQuery("DELETE FROM " + AwardTable.TABLE_NAME + " "
					+ "WHERE " + AwardTable.MOVIE_NAME + " = " + movie.getMovieName() + " "
					+ "and " + AwardTable.YEAR + " = " + movie.getReleaseYear());
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
}
