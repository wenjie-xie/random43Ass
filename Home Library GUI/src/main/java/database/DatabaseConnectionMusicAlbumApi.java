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
			
			connection.close();
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
			connection.close();
			
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
		try {
			
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
			
			connection.close();
			
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
			System.out.println("SELECT * FROM " + MusicTable.TABLE_NAME + " WHERE " + MusicTable.ALBUM_NAME + " = '" + musicAlbumName + "'");
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + MusicTable.TABLE_NAME + " WHERE " + MusicTable.ALBUM_NAME + " = '" + musicAlbumName + "'");
			if (rs.next()) {
				result = rs.getString(MusicTable.ALBUM_NAME);
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return result;
	}
	
	
	/********************************
	 * GET MUSIC ALBUM *
	 ********************************/
	
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
			System.out.println("SELECT " + MusicTable.ALBUM_NAME + ", " + MusicTable.YEAR + " "
					+ "FROM " + MusicTable.TABLE_NAME + " "
					+ "WHERE " + MusicTable.ALBUM_NAME + " = '" + musicAlbumName + "'");
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
				System.out.println("SELECT " + MusicTable.LANGUAGE + " FROM " + MusicTable.TABLE_NAME + " "
						+ "WHERE " + MusicTable.ALBUM_NAME + " = '" + musicAlbumName + "'"
						+ " and " + MusicTable.MUSIC_NAME + " = '" + musicName + "'");
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
			
			connection.close();
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
			System.out.println("SELECT " + MusicTable.MUSIC_NAME + " FROM " + MusicTable.TABLE_NAME + " WHERE " + MusicTable.ALBUM_NAME + " = '" + musicAlbumName + "'");
			ResultSet rs = stmt.executeQuery("SELECT " + MusicTable.MUSIC_NAME + " FROM " + MusicTable.TABLE_NAME + " WHERE " + MusicTable.ALBUM_NAME + " = '" + musicAlbumName + "'");
			
			while (rs.next()) {
				musicNameList.add(rs.getString(MusicTable.MUSIC_NAME));
			}
			
			connection.close();
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
			System.out.println("SELECT * "
					+ "FROM " + PeopleInvolvedMusicTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedMusicTable.ALBUM_NAME + " = " + musicAlbum.getAlbumName() + " "
					+ " and " + PeopleInvolvedMusicTable.YEAR + " = " + musicAlbum.getYearPublished() + " "
					+ " and " + PeopleInvolvedMusicTable.MUSIC_NAME + " = '" + musicName + "'");
			ResultSet rs = stmt.executeQuery("SELECT * "
					+ "FROM " + PeopleInvolvedMusicTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedMusicTable.ALBUM_NAME + " = " + musicAlbum.getAlbumName() + " "
					+ " and " + PeopleInvolvedMusicTable.YEAR + " = " + musicAlbum.getYearPublished() + " "
					+ " and " + PeopleInvolvedMusicTable.MUSIC_NAME + " = '" + musicName + "'");
			
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
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return peopleInvolvedMap;
	}
	
	/**
	 * Get all the singer id of the given music
	 * @param musicAlbum
	 * @param musicName
	 * @return a list of song writer
	 * @throws SQLException
	 */
	private static ArrayList<Integer> getSingerIDList(MusicAlbum musicAlbum, String musicName) throws SQLException {
		ArrayList<Integer> singerList = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			Statement stmt = null;
			stmt = connection.createStatement();
			System.out.println("SELECT * "
					+ "FROM " + MusicSingerTable.TABLE_NAME + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = " + musicAlbum.getAlbumName() + " "
					+ " and " + MusicSingerTable.YEAR + " = " + musicAlbum.getYearPublished() + " "
					+ " and " + MusicSingerTable.MUSIC_NAME + " = '" + musicName + "'");
			ResultSet rs = stmt.executeQuery("SELECT * "
					+ "FROM " + MusicSingerTable.TABLE_NAME + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = " + musicAlbum.getAlbumName() + " "
					+ " and " + MusicSingerTable.YEAR + " = " + musicAlbum.getYearPublished() + " "
					+ " and " + MusicSingerTable.MUSIC_NAME + " = '" + musicName + "'");
			
			while(rs.next()) {
				int personID = Integer.parseInt(rs.getString(MusicSingerTable.PEOPLE_INVOLVED_ID));
				
				singerList.add(personID);
			}
			
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
		
		return singerList;
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
		
		ArrayList<Integer> singerIDList = getSingerIDList(musicAlbum, musicName);
		
		for (int singerID : singerIDList) {
			singerList.add(getPersonFromPeopleInvolvedTable(singerID));
		}
		
		return singerList;
	}
	
	
	/*******************************
	 * UPDATE MUSIC ALBUM *
	 *******************************/
	
	/**
	 * Compare and update the oldMusicAlbum to the newMusicAlbum
	 * @param oldMusicAlbum
	 * @param newMusicAlbum
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public static void compareAndUpdateMusicAlbum(MusicAlbum oldMusicAlbum, MusicAlbum newMusicAlbum) throws NumberFormatException, SQLException {
		// update Music table
		compareAndUpdateMusicTable(oldMusicAlbum, newMusicAlbum);
	
		// update Music singer table
		compareAndUpdateMusicSingerTable(oldMusicAlbum, newMusicAlbum);
		
		// update People involved music table
		compareAndUpdatePeopleInvolvedMusicTable(oldMusicAlbum, newMusicAlbum);
	}
	
	
	/**
	 * compare the music track in both album and return a map to 2 list; "removed", "same" and "new"
	 * @param oldMusicAlbum
	 * @param newMusicAlbum
	 * @return a map of two list one containing a list of music name that should be removed and one containing
	 * a list of music name that should be inserted
	 */
	private static HashMap<String, ArrayList<Music>> determineRemovedSameAndNewMusicTrack(MusicAlbum oldMusicAlbum, MusicAlbum newMusicAlbum) {
		HashMap<String, ArrayList<Music>> result = new HashMap<>();
		ArrayList<Music> removedMusicList = new ArrayList<>();
		ArrayList<Music> newMusicList = new ArrayList<>();
		ArrayList<Music> sameMusicList = new ArrayList<>();
		
		ArrayList<String> oldMusicNameList = getMusicNamesFromMusicList(oldMusicAlbum.getMusicTrackList());
		ArrayList<String> newMusicNameList = getMusicNamesFromMusicList(newMusicAlbum.getMusicTrackList());
		
		// Determine the music that should be removed
		for (int i = 0; i < oldMusicNameList.size(); i++) {
			String oldMusicName = oldMusicNameList.get(i);
			Music oldMusic = oldMusicAlbum.getMusicTrackList().get(i);
			if (!newMusicNameList.contains(oldMusicName)) {
				removedMusicList.add(oldMusic);
			} else {
				sameMusicList.add(oldMusic);
			}
		}
		
		// Determine the music that is new
		for (int i = 0; i < newMusicNameList.size(); i++) {
			String newMusicName = newMusicNameList.get(i);
			Music newMusic = newMusicAlbum.getMusicTrackList().get(i);
			if (!oldMusicNameList.contains(newMusicName)) {
				newMusicList.add(newMusic);
			}
		}
		
		result.put("removed", removedMusicList);
		result.put("new", newMusicList);
		result.put("same", sameMusicList);
		
		return result;
	}
	
	private static ArrayList<String> getMusicNamesFromMusicList(ArrayList<Music> musicList) {
		ArrayList<String> result = new ArrayList<>();
		for (Music music : musicList) {
			result.add(music.getMusicName().replaceAll("'", ""));
		}
		return result;
	}
	
	/**
	 * Compare and update the Music Table
	 * @param oldMusicAlbum
	 * @param newMusicAlbum
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	private static void compareAndUpdateMusicTable(MusicAlbum oldMusicAlbum, MusicAlbum newMusicAlbum) throws NumberFormatException, SQLException {
		
		HashMap<String, ArrayList<Music>> sameRemovedNewMap = determineRemovedSameAndNewMusicTrack(oldMusicAlbum, newMusicAlbum);
		ArrayList<Music> sameMusicList = sameRemovedNewMap.get("same");
		ArrayList<Music> removedMusicList = sameRemovedNewMap.get("removed");
		ArrayList<Music> newMusicList = sameRemovedNewMap.get("new");
		
		try {
			// Remove the music that no longer exist
			for (Music music : removedMusicList) {
				String musicName = music.getMusicName().replaceAll("'", "");
				String albumName = oldMusicAlbum.getAlbumName().replaceAll("'", "");
				int publishedYear = Integer.parseInt(oldMusicAlbum.getYearPublished());
				removeARowFromMusicTable(albumName, publishedYear, musicName);
			}
			
			// insert the new music to the table
			String albumName = newMusicAlbum.getAlbumName().replaceAll("'", "");
			int publishedYear = Integer.parseInt(newMusicAlbum.getYearPublished());
			int diskType = Integer.parseInt(newMusicAlbum.getDiskType());
			Person producer = newMusicAlbum.getProducer();
			MusicAlbum dummyAlbum = new MusicAlbum(albumName, publishedYear, newMusicList);
			dummyAlbum.setDiskType(diskType);
			dummyAlbum.setProducer(producer);
			insertIntoMusic(dummyAlbum);
			
			// update other fields for the music that stays the same
			int producerID = Integer.parseInt(findOrCreatePerson(producer));
			Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword);
			for (Music music : sameMusicList) {
				
				Statement stmt = null;
				stmt = connection.createStatement();
				stmt.executeUpdate("UPDATE " + MusicTable.MUSIC_NAME + " "
						+ "SET " + MusicTable.ALBUM_NAME + " = " + newMusicAlbum.getAlbumName() + ", "
							+ MusicTable.YEAR + " = " + newMusicAlbum.getYearPublished() + ", "
							+ MusicTable.MUSIC_NAME + " = " + music.getMusicName() + ", "
							+ MusicTable.LANGUAGE + " = " + music.getLanguage() + ", "
							+ MusicTable.DISK_TYPE + " = " + newMusicAlbum.getDiskType() + ", "
							+ MusicTable.PRODUCER_ID + " = " + producerID + " "
						+ "WHERE " + MusicTable.ALBUM_NAME + " = " + oldMusicAlbum.getAlbumName() + " "
								+ "and " + MusicTable.YEAR + " = " + oldMusicAlbum.getYearPublished() + " "
								+ "and " + MusicTable.MUSIC_NAME + " = " + music.getMusicName());
			}
			
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	/**
	 * Return the object within the musicList containing with its music name as musicName
	 * @param musicList
	 * @param musicName
	 * @return a Music object, if no such musicName is found then return null
	 */
	private static Music getMusicWithName(ArrayList<Music> musicList, String musicName) {
		Music result = null;
		for (Music music : musicList) {
			if (music.getMusicName().replaceAll("'", "").equals(musicName)) {
				result = music;
			}
		}
		return result;
	}
	
	
	/**
	 * Compare and update music singer table
	 * @param oldMusicAlbum
	 * @param newMusicAlbum
	 * @throws SQLException 
	 */
	private static void compareAndUpdateMusicSingerTable(MusicAlbum oldMusicAlbum, MusicAlbum newMusicAlbum) throws SQLException {
		HashMap<String, ArrayList<Music>> sameRemovedNewMap = determineRemovedSameAndNewMusicTrack(oldMusicAlbum, newMusicAlbum);
		ArrayList<Music> sameMusicList = sameRemovedNewMap.get("same");
		ArrayList<Music> removedMusicList = sameRemovedNewMap.get("removed");
		ArrayList<Music> newMusicList = sameRemovedNewMap.get("new");
		
		try {
			// Remove the music that no longer exists
			String albumName = oldMusicAlbum.getAlbumName().replaceAll("'", "");
			int year = Integer.parseInt(oldMusicAlbum.getYearPublished());
			for (Music music : removedMusicList) {
				String musicName = music.getMusicName().replaceAll("'", "");
				removeMusicFromMusicSingerTable(albumName, year, musicName);
			}
			
			// Remove the singer that no longer exists in each same music
			for (Music music : sameMusicList) {
				String musicName = music.getMusicName().replaceAll("'", "");
				Music oldMusic = getMusicWithName(oldMusicAlbum.getMusicTrackList(), musicName);
				Music newMusic = getMusicWithName(newMusicAlbum.getMusicTrackList(), musicName);
				compareAndUpdateSingersForMusic(albumName, year, oldMusic, newMusic);
			}
			
			// Insert new Music
			for (Music music : newMusicList) {
				insertIntoMusicSinger(newMusicAlbum, music);
			}
			
			// update the old music info with the new music info
			// for the columns such as album name and year
			Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword);
			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("UPDATE " + MusicSingerTable.TABLE_NAME + " "
					+ "SET " + MusicSingerTable.ALBUM_NAME + " = " + newMusicAlbum.getAlbumName() + ", "
					+ MusicSingerTable.YEAR + " = " + newMusicAlbum.getYearPublished() + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = " + oldMusicAlbum.getAlbumName() + " "
							+ "and " + MusicSingerTable.YEAR + " = " + oldMusicAlbum.getYearPublished());
			connection.close();
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	
	/**
	 * MusicSinger Table
	 * Compare and update singer for each music in the Music singer table
	 * @param albumName
	 * @param year
	 * @param oldMusic the old music
	 * @param newMusic the new music
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	private static void compareAndUpdateSingersForMusic(String albumName, int year, Music oldMusic, Music newMusic) throws NumberFormatException, SQLException {
		HashMap<String, ArrayList<Person>> removedSameAndNewMap = determineRemovedSameAndNewPerson(oldMusic.getSingerList(), newMusic.getSingerList());
		ArrayList<Person> sameSingerList = removedSameAndNewMap.get("same");
		ArrayList<Person> newSingerList = removedSameAndNewMap.get("new");
		ArrayList<Person> removedSingerList = removedSameAndNewMap.get("removed");
		
		// remove singer that no longer exists
		for (Person singer : removedSingerList) {
			int singerID = Integer.parseInt(findOrCreatePerson(singer));
			removeARowFromMusicSingerTable(albumName, year, oldMusic.getMusicName(), singerID);
		}
		
		// insert new singer
		String musicName = newMusic.getMusicName().replaceAll("'", "");
		Music dummMusic = new Music(musicName);
		dummMusic.setSingerList(newSingerList);
		MusicAlbum dummyMusicAlbum = new MusicAlbum(albumName, year, null);
		insertIntoMusicSinger(dummyMusicAlbum, dummMusic);
	}
	
	/**
	 * Compare and update PeopleInvolvedMusic Table
	 * @param oldMusicAlbum
	 * @param newMusicAlbum
	 * @throws SQLException 
	 */
	private static void compareAndUpdatePeopleInvolvedMusicTable(MusicAlbum oldMusicAlbum, MusicAlbum newMusicAlbum) throws SQLException {
		// Remove all rows related to the old MusicAlbum
		removeMusicAlbumFromPeopleInvolvedMusicTable(oldMusicAlbum);
		
		// insert everything in the newMusicAlbum
		for (Music music : newMusicAlbum.getMusicTrackList()) {
			insertIntoPeopleInvolvedMusic(newMusicAlbum, music);
		}
	}
	
	/**********************************
	 * REMOVE MUSIC ALBUM *
	 **********************************/
	
	/**
	 * Remove a row from music table
	 * @param albumName with no ''
	 * @param year
	 * @param musicName with no ''
	 * @throws SQLException 
	 */
	private static void removeARowFromMusicTable(String albumName, int year, String musicName) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("DELETE FROM " + MusicTable.TABLE_NAME + " "
					+ "WHERE " + MusicTable.ALBUM_NAME + " = '" + albumName + "' "
					+ "and " + MusicTable.YEAR + " = " + year + " "
					+ "and " + MusicTable.MUSIC_NAME + " = '" + musicName + "'");
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Remove a row from music singer table
	 * @param albumName with no ''
	 * @param year
	 * @param musicName with no ''
	 * @throws SQLException 
	 */
	private static void removeMusicFromMusicSingerTable(String albumName, int year, String musicName) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("DELETE FROM " + MusicSingerTable.TABLE_NAME + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = '" + albumName + "' "
					+ "and " + MusicSingerTable.YEAR + " = " + year + " "
					+ "and " + MusicSingerTable.MUSIC_NAME + " = '" + musicName + "'");
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Remove a row from music singer table
	 * @param albumName with no ''
	 * @param year
	 * @param musicName with no ''
	 * @param singerID
	 * @throws SQLException 
	 */
	private static void removeARowFromMusicSingerTable(String albumName, int year, String musicName, int singerID) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("DELETE FROM " + MusicSingerTable.TABLE_NAME + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = '" + albumName + "' "
					+ "and " + MusicSingerTable.YEAR + " = " + year + " "
					+ "and " + MusicSingerTable.MUSIC_NAME + " = '" + musicName + "' "
					+ "and " + MusicSingerTable.PEOPLE_INVOLVED_ID + " = " + singerID);
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Remove a album from the Music singer table
	 * @param oldMusicAlbum
	 * @throws SQLException 
	 */
	private static void removeMusicAlbumFromMusicSingerTable(MusicAlbum oldMusicAlbum) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("DELETE FROM " + MusicSingerTable.TABLE_NAME + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = " + oldMusicAlbum.getAlbumName());
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Remove a album from the Music table
	 * @param oldMusicAlbum
	 * @throws SQLException 
	 */
	private static void removeMusicAlbumFromMusicTable(MusicAlbum oldMusicAlbum) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("DELETE FROM " + MusicTable.TABLE_NAME + " "
					+ "WHERE " + MusicTable.ALBUM_NAME + " = " + oldMusicAlbum.getAlbumName());
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Remove a album from the People Involved music table
	 * @param oldMusicAlbum
	 * @throws SQLException 
	 */
	private static void removeMusicAlbumFromPeopleInvolvedMusicTable(MusicAlbum oldMusicAlbum) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {

			Statement stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate("DELETE FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedMusicTable.ALBUM_NAME + " = " + oldMusicAlbum.getAlbumName());
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
}
