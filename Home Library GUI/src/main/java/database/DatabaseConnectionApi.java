/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import database.tables.BookAuthorTable;
import database.tables.BookTable;
import database.tables.KeywordTable;
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
	protected static final String sqlPassword = "a";
	
	
	/**
	 * Disable Auto commit
	 * @throws SQLException 
	 */
	protected static void disableAutoCommit() throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SET AUTOCOMMIT = 0");
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
			System.out.println("SET AUTOCOMMIT = 1");
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
			System.out.println("COMMIT");
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
			System.out.println("ROLLBACK");
			stmt.executeUpdate("ROLLBACK");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	protected static String formatString(String str) {
		
		String result = str.replaceAll("'", "");
		
		if (str.length() == 4) {
			result = result.replaceAll("NULL", "").replaceAll("null", "");
		}
		
		if (result.equals("")) {
			result = null;
		}
		return result;
	}
	
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
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = " + person.getSurname() + " "
							+ "and " + PeopleInvolvedTable.FIRST_NAME + " = " + person.getFirstName());
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = " + person.getSurname() + " "
							+ "and " + PeopleInvolvedTable.FIRST_NAME + " = " + person.getFirstName());
			while (rs.next()) {
				// make sure the person exists
				// check both middle name and gender
				String actualMiddleName = rs.getString(PeopleInvolvedTable.MIDDLE_NAME);
				String expectedMiddleName = formatString(person.getMiddleName());
				Integer actualGender = formatStringToInt(rs.getString(PeopleInvolvedTable.GENDER));
				Integer expectedGender = person.getGenderInt();
				
				if (actualGender == expectedGender) {
					if ((actualMiddleName == null && expectedMiddleName == null) || (actualMiddleName.equals(expectedMiddleName))) {
						result = formatStringToInt(rs.getString(PeopleInvolvedTable.ID));
					}
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
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("INSERT INTO " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "(" + PeopleInvolvedTable.FIRST_NAME + ", " + PeopleInvolvedTable.MIDDLE_NAME + ", "
						+ PeopleInvolvedTable.FAMILY_NAME + ", " + PeopleInvolvedTable.GENDER + ") "
					+ "VALUES (" + person.getFirstName() + ", " + person.getMiddleName() + ", "
							+ person.getSurname() + ", " + person.getGender() + ")");
			stmt.executeUpdate("INSERT INTO " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "(" + PeopleInvolvedTable.FIRST_NAME + ", " + PeopleInvolvedTable.MIDDLE_NAME + ", "
						+ PeopleInvolvedTable.FAMILY_NAME + ", " + PeopleInvolvedTable.GENDER + ") "
					+ "VALUES (" + person.getFirstName() + ", " + person.getMiddleName() + ", "
							+ person.getSurname() + ", " + person.getGender() + ")");
			
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
	protected static Person getPersonFromPeopleInvolvedTable(Integer id) throws SQLException {
		Person person;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " WHERE " + PeopleInvolvedTable.ID + " = " + id);
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " WHERE " + PeopleInvolvedTable.ID + " = " + id);
			
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
	private static void updatePeopleInvolvedTableWithNewData(Integer oldID, String columnName, String newData) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("UPDATE " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "SET " + columnName + " = " + newData + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = " + oldID);
			stmt.executeUpdate("UPDATE " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "SET " + columnName + " = " + newData + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = " + oldID);
			
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
	private static void updatePeopleInvolvedTableWithNewData(Integer oldID, String columnName, Integer newData) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("UPDATE " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "SET " + columnName + " = " + newData + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = " + oldID);
			stmt.executeUpdate("UPDATE " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "SET " + columnName + " = " + newData + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = " + oldID);
			
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

			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("DELETE FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = " + person.getSurname() + " "
					+ "and " + PeopleInvolvedTable.FIRST_NAME + " = " + person.getFirstName());
			stmt.executeUpdate("DELETE FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = " + person.getSurname() + " "
					+ "and " + PeopleInvolvedTable.FIRST_NAME + " = " + person.getFirstName());
			
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
	protected static void removePersonFromPeopleInvolvedTable(Integer personID) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("DELETE FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = " + personID);
			stmt.executeUpdate("DELETE FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = " + personID);
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Determine the people that needs to be removed, update or added from the two given people list
	 * @param oldPeopleList
	 * @param newPeopleList
	 * @return a map to each of the three list with keys "removed", "update", and "new"
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
		result.put("update", updatePersonList);
		
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
			
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "SET " + PeopleInvolvedTable.FIRST_NAME + " = " + newPersonInfo.getFirstName() + ", "
							+ PeopleInvolvedTable.MIDDLE_NAME + " = " + newPersonInfo.getMiddleName() + ", "
							+ PeopleInvolvedTable.FAMILY_NAME + " = " + newPersonInfo.getSurname() + ", "
							+ PeopleInvolvedTable.GENDER + " = " + newPersonInfo.getGender() + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = " + personID);
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
}
