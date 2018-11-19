/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.tables.BookAuthorTable;
import database.tables.KeywordTable;
import database.tables.PeopleInvolvedTable;
import items.Book;
import items.Movie;
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
	 * Try to find the ID for the person given in the PeopleInvolvedTable
	 * @param person
	 * @return ID of the person iff the person already exists, else return null
	 * @throws SQLException 
	 */
	protected static String tryToFindPerson(Person person) throws SQLException {
		String result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = " + person.getSurname() + " "
							+ "and " + PeopleInvolvedTable.FIRST_NAME + " = " + person.getFirstName());
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = " + person.getSurname() + " "
							+ "and " + PeopleInvolvedTable.FIRST_NAME + " = " + person.getFirstName());
			if (rs.next()) {
				result = rs.getString(PeopleInvolvedTable.ID);
			}
			
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
	protected static String findOrCreatePerson(Person person) throws SQLException {
		String personID = null;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
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
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " WHERE " + PeopleInvolvedTable.ID + " = " + id);
			
			String firstName = rs.getString(PeopleInvolvedTable.FIRST_NAME);
			String middleName = rs.getString(PeopleInvolvedTable.MIDDLE_NAME);
			String familyName = rs.getString(PeopleInvolvedTable.FAMILY_NAME);
			int gender = Integer.parseInt(rs.getString(PeopleInvolvedTable.GENDER)); 
			
			person = new Person(familyName, firstName);
			person.setMiddleName(middleName);
			person.setGender(gender);
			
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

			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("UPDATE " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "SET " + columnName + " = " + newData + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = " + oldID);
			
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
	private static void updatePeopleInvolvedTableWithNewData(int oldID, String columnName, int newData) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("UPDATE " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "SET " + columnName + " = " + newData + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = " + oldID);
			
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
	protected static void compareAndUpdatePeopleInvolved(int oldPersonID, Person oldPerson, Person newPerson) throws SQLException {
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
			ResultSet rs = stmt.executeQuery("DELETE FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = " + person.getSurname() + " "
					+ "and " + PeopleInvolvedTable.FIRST_NAME + " = " + person.getFirstName());
			
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

			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("DELETE FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.ID + " = " + personID);
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
}
