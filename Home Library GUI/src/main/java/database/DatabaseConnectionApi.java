/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.jndi.cosnaming.CNNameParser;

import database.tables.AwardTable;
import database.tables.BookAuthorTable;
import database.tables.BookTable;
import database.tables.CrewMemberTable;
import database.tables.KeywordTable;
import database.tables.MusicSingerTable;
import database.tables.MusicTable;
import database.tables.PeopleInvolvedMusicTable;
import database.tables.PeopleInvolvedTable;
import items.Book;
import items.Movie;
import items.Music;
import items.MusicAlbum;
import items.Person;

/**
 * @author xiewen4
 * This is where all the function that communicate with the databae
 * goes.
 */
public class DatabaseConnectionApi {
	protected static final String URL = "jdbc:mysql://localhost:3306/HL?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	protected static final String sqlUsername = "root";
	protected static final String sqlPassword = "";
	
	
	/**
	 * Disable Auto commit
	 * @throws SQLException 
	 */
	protected static void disableAutoCommit() throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			//System.out.println("SET AUTOCOMMIT = 0");
			stmt.executeUpdate("SET AUTOCOMMIT = 0");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Enable Auto commit
	 * @throws SQLException 
	 */
	protected static void enableAutoCommit() throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			//System.out.println("SET AUTOCOMMIT = 1");
			stmt.executeUpdate("SET AUTOCOMMIT = 1");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Commit
	 * @throws SQLException 
	 */
	protected static void sqlCommit() throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			//System.out.println("COMMIT");
			stmt.executeUpdate("COMMIT");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Roll back
	 * @throws SQLException 
	 */
	protected static void sqlRollBack() throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			//System.out.println("ROLLBACK");
			stmt.executeUpdate("ROLLBACK");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/*protected static String formatString(String str) {
		
		String result = str.replaceAll("'", "");
		
		if (str.length() == 4) {
			result = result.replaceAll("NULL", "").replaceAll("null", "");
		}
		
		if (result.equals("")) {
			result = null;
		}
		return result;
	}*/
	
	/**
	 * This is to format string to integer
	 * @param num
	 * @return
	 */
	protected static Integer formatStringToInt(String num) {
		
		Integer result = null;
		
		if (num != null) {
			result = Integer.parseInt(num);
		}
		
		return result;
	}
	
	/**
	 * Try to find the ID for the person given in the PeopleInvolvedTable
	 * @param person
	 * @return ID of the person iff the person already exists, else return null
	 * @throws SQLException 
	 */
	protected static Integer tryToFindPerson(Person person) throws SQLException {
		Integer result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = ? "
					+ "and " + PeopleInvolvedTable.FIRST_NAME + " = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString (1, person.getSurname());
		    preparedStatement.setString (2, person.getFirstName());
		    
		    ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				// make sure the person exists
				// check both middle name and gender
				String actualMiddleName = rs.getString(PeopleInvolvedTable.MIDDLE_NAME);
				String expectedMiddleName = person.getMiddleName();
				/*Integer actualGender = formatStringToInt(rs.getString(PeopleInvolvedTable.GENDER));
				Integer expectedGender = person.getGender();*/
				
				// check middle name apparently there won't be a person with the same name but different gender
				if ((actualMiddleName == null && expectedMiddleName == null) || (actualMiddleName.equals(expectedMiddleName))) {
					result = formatStringToInt(rs.getString(PeopleInvolvedTable.ID));
				}
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	/**
	 * Add a new person to PeopleInvolved
	 * @param person the new person that is going to be put into the database
	 * @throws SQLException 
	 */
	protected static void insertIntoPeopleInvolved(Person person) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			String query = "INSERT INTO " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "(" + PeopleInvolvedTable.FIRST_NAME + ", " + PeopleInvolvedTable.MIDDLE_NAME + ", "
					+ PeopleInvolvedTable.FAMILY_NAME + ", " + PeopleInvolvedTable.GENDER + ") "
				+ "VALUES (?, ?, ?, ?)";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, person.getFirstName());
			ps.setString(2, person.getMiddleName());
			ps.setString(3, person.getSurname());
			if (person.getGender() == null) {
				ps.setNull(4, java.sql.Types.INTEGER);
			} else {
				ps.setInt(4, person.getGender());
			}
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * The purpose of this function is to find or create a person in the database
	 * 	- find iff the person exist, otherwise create the person
	 * @param person the person you are trying to find or create
	 * @return the ID of the person
	 * @throws SQLException 
	 */
	protected static Integer findOrCreatePerson(Person person) throws SQLException {
		Integer personID = null;
		try {
			personID = tryToFindPerson(person);
			
			if (personID == null) {
				insertIntoPeopleInvolved(person);
				personID = tryToFindPerson(person);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return personID;
	}
	
	/**
	 * Get Person from PeopleInvolved table
	 * @param id the id of Person in the PeopleInolved Table
	 * @return a Person representing the person with the given id
	 * @throws SQLException 
	 */
	protected static Person getPersonFromPeopleInvolvedTable(int id) throws SQLException {
		Person person;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			String query = "SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			String firstName = rs.getString(PeopleInvolvedTable.FIRST_NAME);
			String middleName = rs.getString(PeopleInvolvedTable.MIDDLE_NAME);
			String familyName = rs.getString(PeopleInvolvedTable.FAMILY_NAME);
			Integer gender = formatStringToInt(rs.getString(PeopleInvolvedTable.GENDER)); 
			
			person = new Person(familyName, firstName);
			person.setMiddleName(middleName);
			person.setGender(gender);
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		return person;
	}
	
	/**
	 * Update the people involved table with the new data
	 * @param oldID
	 * @param columnName
	 * @param newData the new data with ''
	 * @throws SQLException 
	 */
	private static void updatePeopleInvolvedTableWithNewData(int oldID, String columnName, String newData) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			String query = "UPDATE " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "SET " + columnName + " = ? "
					+ "WHERE " + PeopleInvolvedTable.ID + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, newData);
			ps.setInt(2, oldID);
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Update the people involved table with the new data
	 * @param oldID
	 * @param columnName
	 * @param newData the new data as a int
	 * @throws SQLException 
	 */
	private static void updatePeopleInvolvedTableWithNewData(int oldID, String columnName, Integer newData) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			String query = "UPDATE " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "SET " + columnName + " = ? "
					+ "WHERE " + PeopleInvolvedTable.ID + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			if (newData == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, newData);
			}
			ps.setInt(2, oldID);
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Compare and update people involved
	 * @param oldPersonID
	 * @param oldPerson
	 * @param newPerson
	 * @throws SQLException 
	 */
	protected static void compareAndUpdatePeopleInvolved(Integer oldPersonID, Person oldPerson, Person newPerson) throws SQLException {
		try {
			// check if old person id exists
			// FirstName
			if (!oldPerson.getFirstName().equals(newPerson.getFirstName())) {
				updatePeopleInvolvedTableWithNewData(oldPersonID, PeopleInvolvedTable.FIRST_NAME, newPerson.getFirstName());
			}
			
			// MiddleName
			if (!oldPerson.getMiddleName().equals(newPerson.getMiddleName())) {
				updatePeopleInvolvedTableWithNewData(oldPersonID, PeopleInvolvedTable.MIDDLE_NAME, newPerson.getMiddleName());
			}
			
			// LastName
			if (!oldPerson.getSurname().equals(newPerson.getSurname())) {
				updatePeopleInvolvedTableWithNewData(oldPersonID, PeopleInvolvedTable.FAMILY_NAME, newPerson.getSurname());
			}
			
			// Gender
			if (!oldPerson.getGender().equals(newPerson.getGender())) {
				updatePeopleInvolvedTableWithNewData(oldPersonID, PeopleInvolvedTable.GENDER, newPerson.getGender());
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/***********************************
	 * REMOVE *
	 ***********************************/
	
	/**
	 * Delete a person from Person Involved table
	 * @param person
	 * @throws SQLException
	 */
	protected static void removePersonFromPeopleInvolvedTable(Person person) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "DELETE FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = ? "
					+ "and " + PeopleInvolvedTable.FIRST_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, person.getSurname());
			ps.setString(2, person.getFirstName());
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Delete a person from Person Involved table
	 * @param personID
	 * @throws SQLException
	 */
	protected static void removePersonFromPeopleInvolvedTable(int personID) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			String query = "DELETE FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, personID);
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Determine the people that needs to be removed, update or added from the two given people list
	 * @param oldPeopleList
	 * @param newPeopleList
	 * @return a map to each of the three list with keys "removed", "updated", and "new"
	 */
	protected static HashMap<String, ArrayList<Person>> determineRemovedUpdateAndNewPerson(ArrayList<Person> oldPeopleList, ArrayList<Person> newPeopleList) {
		HashMap<String, ArrayList<Person>> result = new HashMap<>();
		// old people that needs to be removed
		ArrayList<Person> removedPersonList = new ArrayList<>();
		// new people that needs to be added
		ArrayList<Person> newPersonList = new ArrayList<>();
		// old people that needs to be updated
		ArrayList<Person> updatePersonList = new ArrayList<>();
		
		
		// if some people are deleted
		if (newPeopleList.size() <= oldPeopleList.size()) {
			for (int i = 0; i < newPeopleList.size(); i++) {
				Person needUpdate = oldPeopleList.get(i);
				updatePersonList.add(needUpdate);
			}
			
			for (int i = newPeopleList.size(); i < oldPeopleList.size(); i++) {
				Person needRemove = oldPeopleList.get(i);
				removedPersonList.add(needRemove);
			}
			
		// if some people are added
		} else {
			for (int i = 0; i < oldPeopleList.size(); i++) {
				Person needUpdate = oldPeopleList.get(i);
				updatePersonList.add(needUpdate);
			}
			
			for (int i = oldPeopleList.size(); i < newPeopleList.size(); i++) {
				Person needAdd = newPeopleList.get(i);
				newPersonList.add(needAdd);
			}
		}
		
		
		result.put("removed", removedPersonList);
		result.put("new", newPersonList);
		result.put("updated", updatePersonList);
		
		return result;
	}
	
	
	/**
	 * The purpose of this method is to update the person with the id given to
	 * the newInfo in the PeopleInvolved Table
	 * @param personID the id of the person you are trying to update
	 * @param newPersonInfo the info you are trying to update the person into
	 * @throws SQLException 
	 */
	protected static void updatePersonWithID(int personID, Person newPersonInfo) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "UPDATE " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "SET " + PeopleInvolvedTable.FIRST_NAME + " = ?, "
					+ PeopleInvolvedTable.MIDDLE_NAME + " = ?, "
					+ PeopleInvolvedTable.FAMILY_NAME + " = ?, "
					+ PeopleInvolvedTable.GENDER + " = ? "
					+ "WHERE " + PeopleInvolvedTable.ID + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, newPersonInfo.getFirstName());
			ps.setString(2, newPersonInfo.getMiddleName());
			ps.setString(3, newPersonInfo.getSurname());
			if (newPersonInfo.getGender() == null)
				ps.setNull(4, java.sql.Types.INTEGER);
			else
				ps.setInt(4, newPersonInfo.getGender());
			ps.setInt(5, personID);
			
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**************************************
	 * Remove person from people Involved *
	 **************************************/
	
	/**
	 * Check is the personID is used at all
	 * @param personID
	 * @return true iff it is used
	 * @throws SQLException
	 */
	private static boolean isPersonIDUsed(int personID) throws SQLException {
		boolean result = false;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String uid = 
					"(SELECT DISTINCT personID "
					+ "FROM "
						+ "(SELECT " + BookAuthorTable.AUTHOR_ID + " AS personID FROM " + BookAuthorTable.TABLE_NAME + ") AS t "
						+ "UNION "
						+ "(SELECT " + PeopleInvolvedMusicTable.PEOPLE_INVOLVED_ID + " AS personID FROM " + PeopleInvolvedMusicTable.TABLE_NAME + ") "
						+ "UNION "
						+ "(SELECT " + CrewMemberTable.PEOPLE_INVOLVED_ID + " AS personID FROM " + CrewMemberTable.TABLE_NAME + ") "
						+ "UNION "
						+ "(SELECT " + MusicSingerTable.PEOPLE_INVOLVED_ID + " AS personID FROM " + MusicSingerTable.TABLE_NAME + ") "
						+ "UNION "
						+ "(SELECT " + MusicTable.PRODUCER_ID + " AS personID FROM " + MusicTable.TABLE_NAME + ") "
						+ "UNION "
						+ "(SELECT " + AwardTable.PEOPLE_INVOLVED_ID + " AS personID FROM " + AwardTable.TABLE_NAME + ")) AS uid";
			
			String query =
					"SELECT personID FROM " + uid + " "
					+ "WHERE personID = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, personID);
			ResultSet rs = ps.executeQuery();
			result = rs.next();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	private static void removeIDFromPeopleInvolvedTable(int personId) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = 
					"DELETE FROM "
						+ PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE "
						+ PeopleInvolvedTable.ID + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, personId);
			ps.executeUpdate();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Clean the unused if from the people involved table
	 * @throws SQLException 
	 */
	public static void cleanPeopleInvolvedTable() throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = 
					"SELECT "
						+ PeopleInvolvedTable.ID + " "
					+ "FROM "
						+ PeopleInvolvedTable.TABLE_NAME;
			
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int personID = rs.getInt(PeopleInvolvedTable.ID);
			
			if (!isPersonIDUsed(personID)) {
				removeIDFromPeopleInvolvedTable(personID);
			}
		}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
}
