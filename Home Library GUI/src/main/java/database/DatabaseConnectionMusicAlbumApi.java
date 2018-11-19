package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import database.tables.BookAuthorTable;
import database.tables.MusicSingerTable;
import database.tables.MusicTable;
import database.tables.PeopleInvolvedMusicTable;
import database.tables.PeopleInvolvedTable;
import database.tables.Role;
import items.Book;
import items.Music;
import items.MusicAlbum;
import items.Person;

public class DatabaseConnectionMusicAlbumApi extends DatabaseConnectionApi {
	private static final String URL = "jdbc:mysql://localhost:3306/HL?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String sqlUsername = "root";
	private static final String sqlPassword = "a";
	
	/******************************
	 * INSERT MUSIC ALBUM *
	 ******************************/
	
	/**
	 * Insert a new music album
	 * @param musicAlbum takes in a musicAlbum object containing all the book info from the front end
	 * @throws SQLException 
	 */
	public static void insertMusicAlbum(MusicAlbum musicAlbum) throws SQLException {
		try {
			// insert into Music table
			insertIntoMusic(musicAlbum);
			
			for (Music music : musicAlbum.getMusicTrackList()) {
				// insert into Music Singer table
				insertIntoMusicSinger(musicAlbum, music);
				
				// insert into People involved music table
				insertIntoPeopleInvolvedMusic(musicAlbum, music);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Try to find the ID for the person given in the PeopleInvolvedTable
	 * @param person
	 * @return ID of the person iff the person already exists, else return null
	 * @throws SQLException 
	 *//*
	private static String tryToFindPerson(Person person) throws SQLException {
		String result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedTable.FAMILY_NAME + " = " + person.getSurname() + " "
							+ "and " + PeopleInvolvedTable.FIRST_NAME + " = " + person.getFirstName() + " "
							+ "and " + PeopleInvolvedTable.MIDDLE_NAME + " = " + person.getMiddleName() + "");
			if (rs.next()) {
				result = rs.getString(PeopleInvolvedTable.ID);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	*//**
	 * Add a new person to PeopleInvolved
	 * @param person the new person that is going to be put into the database
	 * @throws SQLException 
	 *//*
	private static void insertIntoPeopleInvolved(Person person) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("INSERT INTO " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "(" + PeopleInvolvedTable.FIRST_NAME + ", " + PeopleInvolvedTable.MIDDLE_NAME + ", "
						+ PeopleInvolvedTable.FAMILY_NAME + ", " + PeopleInvolvedTable.GENDER + ") "
					+ "VALUES (" + person.getFirstName() + ", " + person.getMiddleName() + ", "
							+ person.getSurname() + ", " + person.getGender() + ")");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	*//**
	 * The purpose of this function is to find or create a person in the database
	 * 	- find iff the person exist, otherwise create the person
	 * @param person the person you are trying to find or create
	 * @return the ID of the person
	 * @throws SQLException 
	 *//*
	private static String findOrCreatePerson(Person person) throws SQLException {
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
	}*/
	
	/**
	 * Insert a new Music Album
	 * @param musicAlbum is a object that contains all the info for an music album
	 * @throws SQLException 
	 */
	private static void insertIntoMusic(MusicAlbum musicAlbum) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			// try to get the producer id
			String producerID = findOrCreatePerson(musicAlbum.getProducer());
					
			// Go through each music in the album and add them
			for (Music currMusic : musicAlbum.getMusicTrackList()) {
				Statement stmt = null;
				stmt = connection.createStatement();
				System.out.println("INSERT INTO " + MusicTable.TABLE_NAME + " "
						+ "(" + MusicTable.ALBUM_NAME + ", " + MusicTable.YEAR + ", " + MusicTable.MUSIC_NAME + ", "
						+ MusicTable.LANGUAGE + ", " + MusicTable.DISK_TYPE + ", " + MusicTable.PRODUCER_ID + ") "
						+ "VALUES (" + musicAlbum.getAlbumName() + ", " + musicAlbum.getYearPublished() + ", "
						+ currMusic.getMusicName() + ", " + currMusic.getLanguage() + ", "
						+ musicAlbum.getDiskType() + ", " + producerID + ")");
				stmt.executeUpdate("INSERT INTO " + MusicTable.TABLE_NAME + " "
						+ "(" + MusicTable.ALBUM_NAME + ", " + MusicTable.YEAR + ", " + MusicTable.MUSIC_NAME + ", "
						+ MusicTable.LANGUAGE + ", " + MusicTable.DISK_TYPE + ", " + MusicTable.PRODUCER_ID + ") "
						+ "VALUES (" + musicAlbum.getAlbumName() + ", " + musicAlbum.getYearPublished() + ", "
						+ currMusic.getMusicName() + ", " + currMusic.getLanguage() + ", "
						+ musicAlbum.getDiskType() + ", " + producerID + ")");
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Insert a new Music singer
	 * @param musicAlbum is a object that contains all the info for an music album
	 * @param music is a object that contains all the info for an music
	 * @throws SQLException 
	 */
	private static void insertIntoMusicSinger(MusicAlbum musicAlbum, Music music) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			// Add each singer
			for (Person singer : music.getSingerList()) {
				String singerID = findOrCreatePerson(singer);
				
				Statement stmt = null;
				stmt = connection.createStatement();
				System.out.println("INSERT INTO " + MusicSingerTable.TABLE_NAME + " "
						+ "("  + MusicSingerTable.ALBUM_NAME + ", " + MusicSingerTable.YEAR + ", "
						+ MusicSingerTable.MUSIC_NAME + ", " + MusicSingerTable.PEOPLE_INVOLVED_ID + ") "
						+ "VALUES (" + musicAlbum.getAlbumName() + ", " + musicAlbum.getYearPublished() + ", "
						+ music.getMusicName() + ", " + singerID + ")");
				stmt.executeUpdate("INSERT INTO " + MusicSingerTable.TABLE_NAME + " "
						+ "("  + MusicSingerTable.ALBUM_NAME + ", " + MusicSingerTable.YEAR + ", "
						+ MusicSingerTable.MUSIC_NAME + ", " + MusicSingerTable.PEOPLE_INVOLVED_ID + ") "
						+ "VALUES (" + musicAlbum.getAlbumName() + ", " + musicAlbum.getYearPublished() + ", "
						+ music.getMusicName() + ", " + singerID + ")");
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Insert a person involved in music into the PeopleInvolvedMusic table
	 * @param musicAlbum is a object that contains all the info for an music album
	 * @param music is a object that contains all the info for an music
	 * @throws SQLException 
	 */
	private static void insertIntoPeopleInvolvedMusic(MusicAlbum musicAlbum, Music music) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			// check if the song writer, composer and the arranger is the same person
			Person sw = music.getSongWriter();
			Person com = music.getComposer();
			Person arr = music.getArranger();
			// All the same
			if (sw.equals(com) && sw.equals(arr)) {
				insertIntoPeopleInvolvedMusicHelper(musicAlbum, music, sw, true, true, true);
				
			} else if (sw.equals(com)) {
				insertIntoPeopleInvolvedMusicHelper(musicAlbum, music, sw, true, true, false);
				insertIntoPeopleInvolvedMusicHelper(musicAlbum, music, arr, false, false, true);
				
			} else if (sw.equals(arr)) {
				insertIntoPeopleInvolvedMusicHelper(musicAlbum, music, sw, true, false, true);
				insertIntoPeopleInvolvedMusicHelper(musicAlbum, music, com, false, true, false);
				
			} else if (com.equals(arr)) {
				insertIntoPeopleInvolvedMusicHelper(musicAlbum, music, com, false, true, true);
				insertIntoPeopleInvolvedMusicHelper(musicAlbum, music, sw, true, false, false);
				
			} else {
				insertIntoPeopleInvolvedMusicHelper(musicAlbum, music, sw, true, false, false);
				insertIntoPeopleInvolvedMusicHelper(musicAlbum, music, com, false, true, false);
				insertIntoPeopleInvolvedMusicHelper(musicAlbum, music, arr, false, false, true);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Insert a person involved in music into the PeopleInvolvedMusic table
	 * @param musicAlbum is a object that contains all the info for an music album
	 * @param music is a object that contains all the info for an music
	 * @param person the person that is involved
	 * @param isSongWriter
	 * @param isComposer
	 * @param isArranger
	 * @throws SQLException 
	 */
	private static void insertIntoPeopleInvolvedMusicHelper(MusicAlbum musicAlbum, Music music, Person person, boolean isSongWriter, boolean isComposer, boolean isArranger) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			// Find or create the person involved if it is new
			String peopleInvolvedID = findOrCreatePerson(person);
			
			// check position to insert properly
			String sw = "NULL";
			String com = "NULL";
			String arr = "NULL";
			if (isSongWriter)
				sw = "1";
			if (isComposer)
				com = "1";
			if (isArranger)
				arr = "1";
				
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("INSERT INTO " + PeopleInvolvedMusicTable.TABLE_NAME + " "
					+ "(" + PeopleInvolvedMusicTable.ALBUM_NAME + ", " + PeopleInvolvedMusicTable.YEAR + ", "
					+ PeopleInvolvedMusicTable.MUSIC_NAME + ", " + PeopleInvolvedMusicTable.PEOPLE_INVOLVED_ID + ", "
					+ PeopleInvolvedMusicTable.IS_SONG_WRITER + ", " + PeopleInvolvedMusicTable.IS_COMPOSER + ", "
					+ PeopleInvolvedMusicTable.IS_ARRANGER + ") "
					+ "VALUES (" + musicAlbum.getAlbumName() + ", " + musicAlbum.getYearPublished() + ", "
					+ music.getMusicName() + ", " + peopleInvolvedID + ", " + sw + ", " + com + ", " + arr + ")");
			stmt.executeUpdate("INSERT INTO " + PeopleInvolvedMusicTable.TABLE_NAME + " "
					+ "(" + PeopleInvolvedMusicTable.ALBUM_NAME + ", " + PeopleInvolvedMusicTable.YEAR + ", "
					+ PeopleInvolvedMusicTable.MUSIC_NAME + ", " + PeopleInvolvedMusicTable.PEOPLE_INVOLVED_ID + ", "
					+ PeopleInvolvedMusicTable.IS_SONG_WRITER + ", " + PeopleInvolvedMusicTable.IS_COMPOSER + ", "
					+ PeopleInvolvedMusicTable.IS_ARRANGER + ") "
					+ "VALUES (" + musicAlbum.getAlbumName() + ", " + musicAlbum.getYearPublished() + ", "
					+ music.getMusicName() + ", " + peopleInvolvedID + ", " + sw + ", " + com + ", " + arr + ")");
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/******************************
	 * UPDATE MUSIC ALBUM *
	 ******************************/
	
	/**
	 * Try to find the music album with the given music album Name
	 * @param musicAlbumName the title of the album being searched
	 * @return the AlbumName iff the movie album title exists in the database, otherwise return null
	 * @throws SQLException 
	 */
	public static String tryToFindMusicAlbumName(String musicAlbumName) throws SQLException {
		String result = null;
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + MusicTable.TABLE_NAME + " WHERE " + MusicTable.ALBUM_NAME + " = '" + musicAlbumName + "'");
			if (rs.next()) {
				result = rs.getString(MusicTable.ALBUM_NAME);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	/**
	 * Get the info of the music album with the given album name
	 * @param musicAlbumName the name of the music album
	 * @return a musicAlbum object containing all the info of the given
	 * music album name
	 * @throws SQLException 
	 */
	public static MusicAlbum getMusicAlbumInfo(String musicAlbumName) throws SQLException {
		MusicAlbum musicAlbum;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			// Get all the music names
			ArrayList<String> musicNames = getMusicNameList(musicAlbumName);
			
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT " + MusicTable.ALBUM_NAME + ", " + MusicTable.YEAR + " "
					+ "FROM " + MusicTable.TABLE_NAME + " "
					+ "WHERE " + MusicTable.ALBUM_NAME + " = '" + musicAlbumName + "'");
			
			String albumName = rs.getString(MusicTable.ALBUM_NAME);
			int year = Integer.parseInt(rs.getString(MusicTable.YEAR));
			int producerID = Integer.parseInt(rs.getString(MusicTable.PRODUCER_ID));
			Person producer = getPersonFromPeopleInvolvedTable(producerID);
			int diskType = Integer.parseInt(rs.getString(MusicTable.DISK_TYPE));
			
			musicAlbum = new MusicAlbum(albumName, year, null);
			
			ArrayList<Music> musicList = new ArrayList<>();
			// Loop through all music
			for (String musicName : musicNames) {
				stmt = null;
				stmt = connection.createStatement();
				rs = stmt.executeQuery("SELECT " + MusicTable.LANGUAGE + " FROM " + MusicTable.TABLE_NAME + " "
						+ "WHERE " + MusicTable.ALBUM_NAME + " = '" + musicAlbumName + "'"
						+ " and " + MusicTable.MUSIC_NAME + " = '" + musicName + "'");
	
				String language = rs.getString(MusicTable.LANGUAGE);
				
				ArrayList<Person> singerList = getSingerList(musicAlbum, musicName);
				HashMap<String, ArrayList<Person>> peopleMap = getPeopleInvolvedMap(musicAlbum, musicName);
				ArrayList<Person> composerList = peopleMap.get(Role.COMPOSER);
				ArrayList<Person> songWriterList = peopleMap.get(Role.SONG_WRITER);
				ArrayList<Person> arrangerList = peopleMap.get(Role.ARRANGER);
				
				Music music = new Music(musicName);
				music.setLanguage(language);
				music.setSingerList(singerList);
				music.setComposer(composerList.get(0));
				music.setArranger(arrangerList.get(0));
				music.setSongWriter(songWriterList.get(0));
				
				musicList.add(music);
			}
			
			musicAlbum.setMusicTrackList(musicList);
					
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return musicAlbum;
	}
	
	/**
	 * Get a list of music name from the in the given music album
	 * @param musicAlbumName the name of the music album
	 * @return a list of music names
	 * @throws SQLException 
	 */
	private static ArrayList<String> getMusicNameList(String musicAlbumName) throws SQLException {
		ArrayList<String> musicNameList = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT " + MusicTable.MUSIC_NAME + " FROM " + MusicTable.TABLE_NAME + " WHERE " + MusicTable.ALBUM_NAME + " = '" + musicAlbumName + "'");
			
			while (rs.next()) {
				musicNameList.add(rs.getString(MusicTable.MUSIC_NAME));
			}
			
					
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return musicNameList;
	}
	
	/**
	 * Get all the people involved in the given music
	 * @param musicAlbum
	 * @param musicName
	 * @return a list of song writer
	 * @throws SQLException
	 */
	private static HashMap<String, ArrayList<Person>> getPeopleInvolvedMap(MusicAlbum musicAlbum, String musicName) throws SQLException {
		HashMap<String, ArrayList<Person>> peopleInvolvedMap = new HashMap<>();
		ArrayList<Person> composerList = new ArrayList<>();
		ArrayList<Person> songWriterList = new ArrayList<>();
		ArrayList<Person> arrangerList = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * "
					+ "FROM " + PeopleInvolvedMusicTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedMusicTable.ALBUM_NAME + " = " + musicAlbum.getAlbumName() + " "
					+ " and " + PeopleInvolvedMusicTable.YEAR + " = " + musicAlbum.getYearPublished() + " "
					+ " and " + PeopleInvolvedMusicTable.MUSIC_NAME + " = " + musicName);
			
			while(rs.next()) {
				int personID = Integer.parseInt(rs.getString(PeopleInvolvedMusicTable.PEOPLE_INVOLVED_ID));
				String isSongWriter = rs.getString(PeopleInvolvedMusicTable.IS_SONG_WRITER);
				String isComposer = rs.getString(PeopleInvolvedMusicTable.IS_COMPOSER);
				String isArranger = rs.getString(PeopleInvolvedMusicTable.IS_ARRANGER);
				
				if (isSongWriter != null) {
					songWriterList.add(getPersonFromPeopleInvolvedTable(personID));	
				}
				
				if (isComposer != null) {
					composerList.add(getPersonFromPeopleInvolvedTable(personID));
				}
				
				if (isArranger != null) {
					arrangerList.add(getPersonFromPeopleInvolvedTable(personID));
				}
			}
			
			peopleInvolvedMap.put(Role.SONG_WRITER, songWriterList);
			peopleInvolvedMap.put(Role.COMPOSER, composerList);
			peopleInvolvedMap.put(Role.ARRANGER, arrangerList);
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return peopleInvolvedMap;
	}
	
	/**
	 * Get all the singer of the given music
	 * @param musicAlbum
	 * @param musicName
	 * @return a list of song writer
	 * @throws SQLException
	 */
	private static ArrayList<Person> getSingerList(MusicAlbum musicAlbum, String musicName) throws SQLException {
		ArrayList<Person> singerList = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * "
					+ "FROM " + MusicSingerTable.TABLE_NAME + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = " + musicAlbum.getAlbumName() + " "
					+ " and " + MusicSingerTable.YEAR + " = " + musicAlbum.getYearPublished() + " "
					+ " and " + MusicSingerTable.MUSIC_NAME + " = " + musicName);
			
			while(rs.next()) {
				int personID = Integer.parseInt(rs.getString(MusicSingerTable.PEOPLE_INVOLVED_ID));
				
				singerList.add(getPersonFromPeopleInvolvedTable(personID));
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return singerList;
	}
}
