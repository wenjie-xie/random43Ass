package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import database.tables.AwardTable;
import database.tables.CrewMemberTable;
import database.tables.MovieTable;
import database.tables.MusicSingerTable;
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
	 */
	public static void insertMovie(Movie movie) {
		try {
			
			// disable auto commit
			disableAutoCommit();
			
			// insert into Movie table
			insertIntoMovie(movie);
			
			// insert into Crew member table
			insertIntoCrewMember(movie);
			
			// insert into Award table
			insertIntoAward(movie);
			
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
			
			String query = "SELECT * FROM " + RoleTable.TABLE_NAME + " "
					+ "WHERE " + RoleTable.DESCRIPTION + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, role);
			ResultSet rs = ps.executeQuery();
			
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
			
			String query = "INSERT INTO " + RoleTable.TABLE_NAME + " "
					+ "(" + RoleTable.DESCRIPTION + ") "
					+ "VALUES (?)";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, role);
			ps.executeUpdate();
			
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
	private static Integer findOrCreateRole(String role) throws SQLException {
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
		
		return Integer.valueOf(roleID);
	}
	
	/**
	 * Insert Movie data to the Movie table
	 * @param movie a movie object holding all Movie info
	 * @throws SQLException 
	 */
	private static void insertIntoMovie(Movie movie) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "INSERT INTO " + MovieTable.TABLE_NAME + " "
					+ "(" + MovieTable.MOVIE_NAME + ", " + MovieTable.YEAR + ") "
					+ "VALUES (?, ?)";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, movie.getMovieName());
			ps.setInt(2, movie.getReleaseYear());
			ps.executeUpdate();
			
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
			int personID = findOrCreatePerson(person);
			// Get the id of the person's role
			int roleID = findOrCreateRole(role);
			
			String query = "INSERT INTO " + CrewMemberTable.TABLE_NAME + " "
					+ "(" + CrewMemberTable.PEOPLE_INVOLVED_ID + ", " + CrewMemberTable.MOVIE_NAME + ", "
					+ CrewMemberTable.RELEASE_YEAR + ", " + CrewMemberTable.ROLE_ID + ") "
					+ "VALUES (?, ?, ?, ?)";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, personID);
			ps.setString(2, movie.getMovieName());
			ps.setInt(3, movie.getReleaseYear());
			ps.setInt(4, roleID);
			ps.executeUpdate();
			
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
				Integer castID = findOrCreatePerson(cast);
				
				String query = "INSERT INTO " + AwardTable.TABLE_NAME + " "
						+ "(" + AwardTable.PEOPLE_INVOLVED_ID + ", " + AwardTable.MOVIE_NAME + ", "
						+ AwardTable.YEAR + ", " + AwardTable.AWARD + ") "
						+ "VALUES (?, ?, ?, ?)";
				
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setInt(1, castID);
				ps.setString(2, movie.getMovieName());
				ps.setInt(3, movie.getReleaseYear());
				ps.setInt(4, cast.getAward());
				ps.executeUpdate();
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
	 */
	public static String tryToFindMovieName(String movieName) {
		String result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			// disable auto commit
			disableAutoCommit();
			
			String query = "SELECT * FROM " + MovieTable.TABLE_NAME + " "
					+ "WHERE " + MovieTable.MOVIE_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, movieName);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				result = rs.getString(MovieTable.MOVIE_NAME);
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
	
	
	/***********************************
	 * GET MOVIE *
	 ***********************************/
	
	/**
	 * Get the info of a given movieName
	 * @param movieName the name of a movie
	 * @return a movie object containing all the info
	 */
	public static Movie getMovieInfo(String movieName) {
		Movie movie = null;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			// disable auto commit
			disableAutoCommit();
			
			String query = "SELECT * FROM " + MovieTable.TABLE_NAME + " "
					+ "WHERE " + MovieTable.MOVIE_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, movieName);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			Integer year = formatStringToInt(rs.getString(MovieTable.YEAR));
			
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
			
			connection.close();
			
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
			
			String query = "SELECT " + RoleTable.DESCRIPTION + " FROM " + RoleTable.TABLE_NAME + " "
					+ "WHERE " + RoleTable.ID + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
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
			
			String query = "SELECT * FROM " + CrewMemberTable.TABLE_NAME + " "
					+ "WHERE " + CrewMemberTable.MOVIE_NAME + " = ? "
					+ "and " + CrewMemberTable.RELEASE_YEAR + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, movie.getMovieName());
			ps.setInt(2, movie.getReleaseYear());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Integer roleID = formatStringToInt(rs.getString(CrewMemberTable.ROLE_ID));
				roleIDList.add(roleID);
				
				Integer personID = formatStringToInt(rs.getString(CrewMemberTable.PEOPLE_INVOLVED_ID));
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
			
			HashMap<String, ArrayList<Integer>> personIDRoleIDMap = getPersonIDRoleIDLists(movie);
			ArrayList<Integer> personIDList = personIDRoleIDMap.get("personID");
			ArrayList<Integer> roleIDList = personIDRoleIDMap.get("roleID");
			
			for (int i = 0; i < personIDList.size(); i++) {
				Integer roleID = roleIDList.get(i);
				String role = getRoleDescription(roleID);
				
				Integer personID = personIDList.get(i);
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
					Integer award = getAwardFromAwardTable(personID, movie.getMovieName(), movie.getReleaseYear());
					person.setAward(award);
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
	 * Get the award of the given person
	 * @param peopleInvolvedID
	 * @param movieName
	 * @param year
	 * @return award of the person, can be null
	 * @throws SQLException 
	 */
	private static Integer getAwardFromAwardTable(int peopleInvolvedID, String movieName, int year) throws SQLException {
		Integer award = null;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "SELECT " + AwardTable.AWARD + " "
					+ "FROM " + AwardTable.TABLE_NAME + " "
					+ "WHERE " + AwardTable.PEOPLE_INVOLVED_ID + " = ? "
							+ "and " + AwardTable.MOVIE_NAME + " = ? "
							+ "and " + AwardTable.YEAR + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, peopleInvolvedID);
			ps.setString(2, movieName);
			ps.setInt(3, year);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
				award = rs.getInt(AwardTable.AWARD);
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return award;
	}
	
	/*********************
	 * UPDATE MOVIE *
	 *********************/
	
	/**
	 * Compare and update the old movie info in the data base into the new movie info given
	 * @param oldMovieInfo
	 * @param newMovieInfo
	 */
	public static void compareAndUpdateMovie(Movie oldMovieInfo, Movie newMovieInfo) {
		try {
			// disable auto commit
			disableAutoCommit();
			
			// compare and update Movie Table
			compareAndUpdateMovieTable(oldMovieInfo, newMovieInfo);
			
			// compare and update Crew member table
			compareAndUpdateCrewMemberTable(oldMovieInfo, newMovieInfo);
			
			// compare and update Award table
			compareAndUpdateAwardTable(oldMovieInfo, newMovieInfo);
		
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
	}
	
	
	/**
	 * Compare and update movie table
	 * @param oldMovieInfo
	 * @param newMovieInfo
	 * @throws SQLException 
	 */
	private static void compareAndUpdateMovieTable(Movie oldMovieInfo, Movie newMovieInfo) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "UPDATE " + MovieTable.TABLE_NAME + " "
					+ "SET " + MovieTable.MOVIE_NAME + " = ?, "
					+ MovieTable.YEAR + " = ? "
					+ "WHERE " + MovieTable.MOVIE_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, newMovieInfo.getMovieName());
			ps.setInt(2, newMovieInfo.getReleaseYear());
			ps.setString(3, oldMovieInfo.getMovieName());
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Compare and update crew member table
	 * @param oldMovieInfo
	 * @param newMovieInfo
	 * @throws SQLException 
	 */
	private static void compareAndUpdateCrewMemberTable(Movie oldMovieInfo, Movie newMovieInfo) throws SQLException {

		String movieName =oldMovieInfo.getMovieName();
		Integer releaseYear = oldMovieInfo.getReleaseYear();
		
		// Director
		updateEachCrewMember(movieName, releaseYear, oldMovieInfo.getDirectorList(), newMovieInfo.getDirectorList(), Role.DIRECTOR);
		// Script Writer
		updateEachCrewMember(movieName, releaseYear, oldMovieInfo.getScriptWriterList(), newMovieInfo.getScriptWriterList(), Role.SCRIPT_WRITER);
		// Cast
		updateEachCrewMember(movieName, releaseYear, oldMovieInfo.getCastList(), newMovieInfo.getCastList(), Role.CAST);
		// Producer
		updateEachCrewMember(movieName, releaseYear, oldMovieInfo.getProducerList(), newMovieInfo.getProducerList(), Role.PRODUCER);
		// Composer
		updateEachCrewMember(movieName, releaseYear, oldMovieInfo.getComposerList(), newMovieInfo.getComposerList(), Role.COMPOSER);
		// Editor
		updateEachCrewMember(movieName, releaseYear, oldMovieInfo.getEditorList(), newMovieInfo.getEditorList(), Role.EDITOR);
		// Costume Designer
		updateEachCrewMember(movieName, releaseYear, oldMovieInfo.getCostumeDesignerList(), newMovieInfo.getCostumeDesignerList(), Role.COSTUME_DESIGNER);
	}
	
	
	/**
	 * Remove, update or add each type of user to crew member table
	 * @param movieName with out ''
	 * @param releaseYear
	 * @param oldPeopleList
	 * @param newPeopleList
	 * @param role
	 * @throws SQLException 
	 */
	private static void updateEachCrewMember(
			String movieName, Integer releaseYear, ArrayList<Person> oldPeopleList, ArrayList<Person> newPeopleList, String role) throws SQLException {
		HashMap<String, ArrayList<Person>> removedUpdatedAndNewMap = determineRemovedUpdateAndNewPerson(oldPeopleList, newPeopleList);
		ArrayList<Person> removedPeopleList = removedUpdatedAndNewMap.get("removed");
		ArrayList<Person> updatedPeopleList = removedUpdatedAndNewMap.get("updated");
		ArrayList<Person> addedPeopleList = removedUpdatedAndNewMap.get("new");
		
		// remove people in removed people list
		for (Person person : removedPeopleList) {
			int peopleInvolvedID = tryToFindPerson(person);
			int roleID = formatStringToInt(tryToFindRole(role));
			removeARowFromCrewMemberTable(peopleInvolvedID, movieName, releaseYear, roleID);
		}
		
		// update people in the updatedPeopleList
		for (int i = 0; i < updatedPeopleList.size(); i++) {
			Person oldPersonInfo = updatedPeopleList.get(i);
			Person newPersonInfo = newPeopleList.get(i);
			int peopleInvolvedID = tryToFindPerson(oldPersonInfo);
			updatePersonWithID(peopleInvolvedID, newPersonInfo);
			
		}
		
		// add new people to the crew
		for (Person person : addedPeopleList) {
			Movie dummyMovie = new Movie(movieName, releaseYear);
			insertIntoCrewMemberHelper(dummyMovie, person, role);
		}
	}
	
	
	/**
	 * Update the award receive by each cast
	 * @param oldMovieInfo
	 * @param newMovieInfo
	 * @throws SQLException 
	 */
	private static void compareAndUpdateAwardTable(Movie oldMovieInfo, Movie newMovieInfo) throws SQLException {
		HashMap<String, ArrayList<Person>> removedUpdatedAndNewMap = determineRemovedUpdateAndNewPerson(oldMovieInfo.getCastList(), newMovieInfo.getCastList());
		ArrayList<Person> removedPeopleList = removedUpdatedAndNewMap.get("removed");
		ArrayList<Person> updatedPeopleList = removedUpdatedAndNewMap.get("updated");
		ArrayList<Person> addedPeopleList = removedUpdatedAndNewMap.get("new");
		
		String movieName = oldMovieInfo.getMovieName();
		Integer releaseYear = oldMovieInfo.getReleaseYear();
		
		// remove people in removed people list
		for (Person person : removedPeopleList) {
			int peopleInvolvedID = tryToFindPerson(person);
			Integer award = person.getAward();
			removeARowFromAwardTable(peopleInvolvedID, movieName, releaseYear, award);
			
		}
		
		// update people in the updatedPeopleList
		for (int i = 0; i < updatedPeopleList.size(); i++) {
			Person oldPersonInfo = updatedPeopleList.get(i);
			Person newPersonInfo = newMovieInfo.getCastList().get(i);
			int peopleInvolvedID = tryToFindPerson(oldPersonInfo);
			updatePersonWithID(peopleInvolvedID, newPersonInfo);
			
		}
		
		// add new people to the award table
		Movie dummyMovie = new Movie(movieName, releaseYear);
		dummyMovie.setCastList(addedPeopleList);
		insertIntoAward(dummyMovie);
	}
	
	/*****************************
	 * REMOVE MOVIE *
	 *****************************/
	
	/**
	 * Remove a row from the crew member table
	 * @param peopleInvolvedID
	 * @param movieName
	 * @param releaseYear
	 * @param roleID should be a Role constant
	 * @throws SQLException
	 */
	private static void removeARowFromCrewMemberTable(int peopleInvolvedID, String movieName, Integer releaseYear, int roleID) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "DELETE FROM " + CrewMemberTable.TABLE_NAME + " "
					+ "WHERE " + CrewMemberTable.PEOPLE_INVOLVED_ID + " = ? "
					+ "and " + CrewMemberTable.MOVIE_NAME + " = ? "
					+ "and " + CrewMemberTable.RELEASE_YEAR + " = ? "
					+ "and " + CrewMemberTable.ROLE_ID + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, peopleInvolvedID);
			ps.setString(2, movieName);
			ps.setInt(3, releaseYear);
			ps.setInt(4, roleID);
			ps.executeUpdate();
			
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Remove a row from the award table
	 * @param peopleInvolvedID
	 * @param movieName 
	 * @param releaseYear
	 * @param award 
	 * @throws SQLException
	 */
	private static void removeARowFromAwardTable(int peopleInvolvedID, String movieName, int releaseYear, Integer award) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "DELETE FROM " + AwardTable.TABLE_NAME + " "
					+ "WHERE " + AwardTable.PEOPLE_INVOLVED_ID + " = ? "
					+ "and " + AwardTable.MOVIE_NAME + " = ? "
					+ "and " + AwardTable.YEAR + " = ? "
					+ "and " + AwardTable.AWARD + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, peopleInvolvedID);
			ps.setString(2, movieName);
			ps.setInt(3, releaseYear);
			if (award == null)
				ps.setNull(4, java.sql.Types.INTEGER);
			else
				ps.setInt(4, award);
			ps.executeUpdate();
			
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Remove the movie given completely from the database
	 * @param movie
	 */
	public static void removeMovie(Movie movie) {
		try {
			// disable auto commit
			disableAutoCommit();
			
			// remove movie from Movie table
			removeMovieFromMovieTable(movie);
			
			// remove movie from crew member table
			removeMovieFromCrewMemberTable(movie);
			
			// remove movie from award table
			removeMovieFromAwardTable(movie);
			
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
	}
	
	
	/**
	 * Remove movie from Movie Table
	 * @throws SQLException 
	 */
	private static void removeMovieFromMovieTable(Movie movie) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "DELETE FROM " + MovieTable.TABLE_NAME + " "
					+ "WHERE " + MovieTable.MOVIE_NAME + " = ? "
					+ "and " + MovieTable.YEAR + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, movie.getMovieName());
			ps.setInt(2, movie.getReleaseYear());
			ps.executeUpdate();
			
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
			
			String query = "DELETE FROM " + CrewMemberTable.TABLE_NAME + " "
					+ "WHERE " + CrewMemberTable.MOVIE_NAME + " = " + movie.getMovieName() + " "
					+ "and " + CrewMemberTable.RELEASE_YEAR + " = " + movie.getReleaseYear();
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, movie.getMovieName());
			ps.setInt(2, movie.getReleaseYear());
			ps.executeUpdate();
			
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
			
			String query = "DELETE FROM " + AwardTable.TABLE_NAME + " "
					+ "WHERE " + AwardTable.MOVIE_NAME + " = " + movie.getMovieName() + " "
					+ "and " + AwardTable.YEAR + " = " + movie.getReleaseYear();
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, movie.getMovieName());
			ps.setInt(2, movie.getReleaseYear());
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
}
