package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

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
	 */
	public static void insertMusicAlbum(MusicAlbum musicAlbum) {
		try {
			// disable auto commit
			disableAutoCommit();
			
			// insert into Music table
			insertIntoMusic(musicAlbum);
			
			for (Music music : musicAlbum.getMusicTrackList()) {
				// insert into Music Singer table
				insertIntoMusicSinger(musicAlbum, music);
				
				// insert into People involved music table
				insertIntoPeopleInvolvedMusic(musicAlbum, music);
			}
			
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
	}
	
	
	/**
	 * Insert a new Music Album
	 * @param musicAlbum is a object that contains all the info for an music album
	 * @throws SQLException 
	 */
	private static void insertIntoMusic(MusicAlbum musicAlbum) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			// try to get the producer id
			Integer producerID = findOrCreatePerson(musicAlbum.getProducer());
					
			// Go through each music in the album and add them
			for (Music currMusic : musicAlbum.getMusicTrackList()) {
				
				String query = "INSERT INTO " + MusicTable.TABLE_NAME + " "
						+ "(" + MusicTable.ALBUM_NAME + ", " + MusicTable.YEAR + ", " + MusicTable.MUSIC_NAME + ", "
						+ MusicTable.LANGUAGE + ", " + MusicTable.DISK_TYPE + ", " + MusicTable.PRODUCER_ID + ") "
						+ "VALUES (?, ?, ?, ?, ?, ?)";
				
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1, musicAlbum.getAlbumName());
				ps.setInt(2, musicAlbum.getYearPublished());
				ps.setString(3, currMusic.getMusicName());
				ps.setString(4, currMusic.getLanguage());
				if (musicAlbum.getDiskType() == null)
					ps.setNull(5, java.sql.Types.INTEGER);
				else
					ps.setInt(5, musicAlbum.getDiskType());
				ps.setInt(6, producerID);
				
				ps.executeUpdate();
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
				Integer singerID = findOrCreatePerson(singer);
				
				String query = "INSERT INTO " + MusicSingerTable.TABLE_NAME + " "
						+ "("  + MusicSingerTable.ALBUM_NAME + ", " + MusicSingerTable.YEAR + ", "
						+ MusicSingerTable.MUSIC_NAME + ", " + MusicSingerTable.PEOPLE_INVOLVED_ID + ") "
						+ "VALUES (?, ?, ?, ?)";
				
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1, musicAlbum.getAlbumName());
				ps.setInt(2, musicAlbum.getYearPublished());
				ps.setString(3, music.getMusicName());
				ps.setInt(4, singerID);
				ps.executeUpdate();
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
			Integer peopleInvolvedID = findOrCreatePerson(person);
			
			// check position to insert properly
			Integer sw = null;
			Integer com = null;
			Integer arr = null;
			if (isSongWriter)
				sw = 1;
			if (isComposer)
				com = 1;
			if (isArranger)
				arr = 1;
			
			
			String query = "INSERT INTO " + PeopleInvolvedMusicTable.TABLE_NAME + " "
					+ "(" + PeopleInvolvedMusicTable.ALBUM_NAME + ", " + PeopleInvolvedMusicTable.YEAR + ", "
					+ PeopleInvolvedMusicTable.MUSIC_NAME + ", " + PeopleInvolvedMusicTable.PEOPLE_INVOLVED_ID + ", "
					+ PeopleInvolvedMusicTable.IS_SONG_WRITER + ", " + PeopleInvolvedMusicTable.IS_COMPOSER + ", "
					+ PeopleInvolvedMusicTable.IS_ARRANGER + ") "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, musicAlbum.getAlbumName());
			ps.setInt(2, musicAlbum.getYearPublished());
			ps.setString(3, music.getMusicName());
			ps.setInt(4, peopleInvolvedID);
			ps.setInt(5, sw);
			ps.setInt(6, com);
			ps.setInt(7, arr);
			ps.executeUpdate();
			
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
			// disable auto commit
			disableAutoCommit();
			
			String query = "SELECT * FROM " + MusicTable.TABLE_NAME + " "
					+ "WHERE " + MusicTable.ALBUM_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, musicAlbumName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getString(MusicTable.ALBUM_NAME);
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
	
	
	/********************************
	 * GET MUSIC ALBUM *
	 ********************************/
	
	/**
	 * Get the info of the music album with the given album name
	 * @param musicAlbumName the name of the music album
	 * @return a musicAlbum object containing all the info of the given
	 * music album name
	 */
	public static MusicAlbum getMusicAlbumInfo(String musicAlbumName) {
		MusicAlbum musicAlbum = null;
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			// disable auto commit
			disableAutoCommit();
			
			// Get all the music names
			ArrayList<String> musicNames = getMusicNameList(musicAlbumName);
			
			String query = "SELECT * "
					+ "FROM " + MusicTable.TABLE_NAME + " "
					+ "WHERE " + MusicTable.ALBUM_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, musicAlbumName);
			ResultSet rs = ps.executeQuery();
			
			
			rs.next();
			String albumName = rs.getString(MusicTable.ALBUM_NAME);
			Integer year = formatStringToInt(rs.getString(MusicTable.YEAR));
			Integer producerID = formatStringToInt(rs.getString(MusicTable.PRODUCER_ID));
			Person producer = getPersonFromPeopleInvolvedTable(producerID);
			Integer diskType = formatStringToInt(rs.getString(MusicTable.DISK_TYPE));
			
			musicAlbum = new MusicAlbum(albumName, year, null);
			musicAlbum.setProducer(producer);
			musicAlbum.setDiskType(diskType);
			
			ArrayList<Music> musicList = new ArrayList<>();
			// Loop through all music
			for (String musicName : musicNames) {
				
				query = "SELECT " + MusicTable.LANGUAGE + " FROM " + MusicTable.TABLE_NAME + " "
						+ "WHERE " + MusicTable.ALBUM_NAME + " = ?"
						+ " and " + MusicTable.MUSIC_NAME + " = ?";
				
				ps = connection.prepareStatement(query);
				ps.setString(1, musicAlbumName);
				ps.setString(2, musicName);
				rs = ps.executeQuery();
				
	
				rs.next();
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
			
			String query = "SELECT " + MusicTable.MUSIC_NAME + " "
					+ "FROM " + MusicTable.TABLE_NAME + " "
					+ "WHERE " + MusicTable.ALBUM_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, musicAlbumName);
			ResultSet rs = ps.executeQuery();
			
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
			
			String query = "SELECT * "
					+ "FROM " + PeopleInvolvedMusicTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedMusicTable.ALBUM_NAME + " = ?"
					+ " and " + PeopleInvolvedMusicTable.YEAR + " = ?"
					+ " and " + PeopleInvolvedMusicTable.MUSIC_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, musicAlbum.getAlbumName());
			ps.setInt(2, musicAlbum.getYearPublished());
			ps.setString(3, musicName);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Integer personID = formatStringToInt(rs.getString(PeopleInvolvedMusicTable.PEOPLE_INVOLVED_ID));
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
			
			String query = "SELECT " + MusicSingerTable.PEOPLE_INVOLVED_ID + " "
					+ "FROM " + MusicSingerTable.TABLE_NAME + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = ?"
					+ " and " + MusicSingerTable.YEAR + " = ? "
					+ " and " + MusicSingerTable.MUSIC_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, musicAlbum.getAlbumName());
			ps.setInt(2, musicAlbum.getYearPublished());
			ps.setString(3, musicName);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Integer personID = formatStringToInt(rs.getString(MusicSingerTable.PEOPLE_INVOLVED_ID));
				
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
		
		for (Integer singerID : singerIDList) {
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
		try {
			// disable auto commit
			disableAutoCommit();
			
			// update Music table
			compareAndUpdateMusicTable(oldMusicAlbum, newMusicAlbum);
		
			// update Music singer table
			compareAndUpdateMusicSingerTable(oldMusicAlbum, newMusicAlbum);
			
			// update People involved music table
			compareAndUpdatePeopleInvolvedMusicTable(oldMusicAlbum, newMusicAlbum);
			
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
	}
	
	
	/**
	 * Compare the two music track given and return a map to 2 list; "removed", "updated" and "new"
	 * @param oldMusicTrackList
	 * @param newMusicTrackList
	 * @return a map of two list one containing a list of music name that should be removed, updated and added
	 */
	private static HashMap<String, ArrayList<Music>> determineRemovedUpdatedAndNewMusicTrack(ArrayList<Music> oldMusicTrackList, ArrayList<Music> newMusicTrackList) {
		HashMap<String, ArrayList<Music>> result = new HashMap<>();
		ArrayList<Music> removedMusicList = new ArrayList<>();
		ArrayList<Music> newMusicList = new ArrayList<>();
		ArrayList<Music> updatedMusicList = new ArrayList<>();
		
		// if some music are deleted
		if (newMusicTrackList.size() <= oldMusicTrackList.size()) {
			for (int i = 0; i < newMusicTrackList.size(); i++) {
				Music needUpdate = oldMusicTrackList.get(i);
				updatedMusicList.add(needUpdate);
			}
			
			for (int i = newMusicTrackList.size(); i < oldMusicTrackList.size(); i++) {
				Music needRemove = oldMusicTrackList.get(i);
				removedMusicList.add(needRemove);
			}
			
		// if some tags are added
		} else {
			for (int i = 0; i < oldMusicTrackList.size(); i++) {
				Music needUpdate = oldMusicTrackList.get(i);
				updatedMusicList.add(needUpdate);
			}
			
			for (int i = oldMusicTrackList.size(); i < newMusicTrackList.size(); i++) {
				Music needAdd = newMusicTrackList.get(i);
				newMusicList.add(needAdd);
			}
		}
		
		result.put("removed", removedMusicList);
		result.put("new", newMusicList);
		result.put("updated", updatedMusicList);
		
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
		
		HashMap<String, ArrayList<Music>> removedUpdatedNewMap = determineRemovedUpdatedAndNewMusicTrack(oldMusicAlbum.getMusicTrackList(), newMusicAlbum.getMusicTrackList());
		ArrayList<Music> updatedMusicList = removedUpdatedNewMap.get("updated");
		ArrayList<Music> removedMusicList = removedUpdatedNewMap.get("removed");
		ArrayList<Music> newMusicList = removedUpdatedNewMap.get("new");
		
		try {
			// Remove the music that no longer exist
			for (Music music : removedMusicList) {
				String musicName = music.getMusicName();
				String albumName = oldMusicAlbum.getAlbumName();
				Integer publishedYear = oldMusicAlbum.getYearPublished();
				removeARowFromMusicTable(albumName, publishedYear, musicName);
			}
			
			// insert the new music to the table
			String albumName = newMusicAlbum.getAlbumName();
			Integer publishedYear = newMusicAlbum.getYearPublished();
			Integer diskType = newMusicAlbum.getDiskType();
			Person producer = newMusicAlbum.getProducer();
			MusicAlbum dummyAlbum = new MusicAlbum(albumName, publishedYear, newMusicList);
			dummyAlbum.setDiskType(diskType);
			dummyAlbum.setProducer(producer);
			insertIntoMusic(dummyAlbum);
			
			// update other fields for the music that stays the same
			Integer producerID = findOrCreatePerson(producer);
			Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword);
			for (int i = 0; i < updatedMusicList.size(); i++) {
				Music updateFrom = updatedMusicList.get(i);
				Music updateTo = newMusicAlbum.getMusicTrackList().get(i);
				
				String query = "UPDATE " + MusicTable.TABLE_NAME + " "
						+ "SET " + MusicTable.ALBUM_NAME + " = ?, "
						+ MusicTable.YEAR + " = ?, "
						+ MusicTable.MUSIC_NAME + " = ?, "
						+ MusicTable.LANGUAGE + " = ?, "
						+ MusicTable.DISK_TYPE + " = ?, "
						+ MusicTable.PRODUCER_ID + " = ? "
					+ "WHERE " + MusicTable.ALBUM_NAME + " = ? "
							+ "and " + MusicTable.YEAR + " = ? "
							+ "and " + MusicTable.MUSIC_NAME + " = ?";
				
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1, newMusicAlbum.getAlbumName());
				ps.setInt(2, newMusicAlbum.getYearPublished());
				ps.setString(3, updateTo.getMusicName());
				ps.setString(4, updateTo.getLanguage());
				if (newMusicAlbum.getDiskType() == null)
					ps.setNull(5, java.sql.Types.INTEGER);
				else
					ps.setInt(5, newMusicAlbum.getDiskType());
				ps.setInt(6, producerID);
				ps.setString(7, oldMusicAlbum.getAlbumName());
				ps.setInt(8, oldMusicAlbum.getYearPublished());
				ps.setString(9, updateFrom.getMusicName());
				
				ps.executeUpdate();
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
			if (music.getMusicName().equals(musicName)) {
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
		HashMap<String, ArrayList<Music>> sameRemovedNewMap = determineRemovedUpdatedAndNewMusicTrack(oldMusicAlbum.getMusicTrackList(), newMusicAlbum.getMusicTrackList());
		ArrayList<Music> updatedMusicList = sameRemovedNewMap.get("updated");
		ArrayList<Music> removedMusicList = sameRemovedNewMap.get("removed");
		ArrayList<Music> newMusicList = sameRemovedNewMap.get("new");
		
		try {
			// Remove the music that no longer exists
			String albumName = oldMusicAlbum.getAlbumName();
			Integer year = oldMusicAlbum.getYearPublished();
			for (Music music : removedMusicList) {
				String musicName = music.getMusicName();
				removeMusicFromMusicSingerTable(albumName, year, musicName);
			}
			
			// Remove the singer that no longer exists or add new singers in each updated music
			for (int i = 0; i < updatedMusicList.size(); i++) {
				Music oldMusic = updatedMusicList.get(i);
				Music newMusic = newMusicAlbum.getMusicTrackList().get(i);
				compareAndUpdateSingersForMusic(albumName, year, oldMusic, newMusic);
				
				// update the old music info with the new music info
				// for the columns such as album name music name and year
				// Ant of these change won't do anything but will return a exception and that is the point!
				Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword);
				
				String query = "UPDATE " + MusicSingerTable.TABLE_NAME + " "
						+ "SET " + MusicSingerTable.ALBUM_NAME + " = ?, "
						+ MusicSingerTable.YEAR + " = ?, "
						+ MusicSingerTable.MUSIC_NAME + " = ? "
						+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = ? "
						+ "and " + MusicSingerTable.YEAR + " = ? "
						+ "and " + MusicSingerTable.MUSIC_NAME + " = ?";
				
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1, newMusicAlbum.getAlbumName());
				ps.setInt(2, newMusicAlbum.getYearPublished());
				ps.setString(3, newMusic.getMusicName());
				ps.setString(4, oldMusicAlbum.getAlbumName());
				ps.setInt(5, oldMusicAlbum.getYearPublished());
				ps.setString(6, oldMusic.getMusicName());
				ps.executeUpdate();
				
				connection.close();
			}
			
			// Insert new Music
			for (Music music : newMusicList) {
				insertIntoMusicSinger(newMusicAlbum, music);
			}
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	
	
	/**
	 * MusicSinger Table
	 * Compare and update singer for each music in the Music singer table
	 * REQ: oldMusicSinger and newMusicSinger must have the same length
	 * 
	 * @param albumName
	 * @param year
	 * @param oldMusicSingerList the old music singer list
	 * @param newMusicSinger the new music
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	private static void compareAndUpdateSingersForMusic(String albumName, Integer year, Music oldMusic, Music newMusic) throws NumberFormatException, SQLException {
		HashMap<String, ArrayList<Person>> removedUpdatedAndNewMap = determineRemovedUpdateAndNewPerson(oldMusic.getSingerList(), newMusic.getSingerList());
		ArrayList<Person> updatedSingerList = removedUpdatedAndNewMap.get("updated");
		ArrayList<Person> newSingerList = removedUpdatedAndNewMap.get("new");
		ArrayList<Person> removedSingerList = removedUpdatedAndNewMap.get("removed");
		
		// remove singer that no longer exists
		for (Person singer : removedSingerList) {
			Integer singerID = tryToFindPerson(singer);
			removeARowFromMusicSingerTable(albumName, year, oldMusic.getMusicName(), singerID);
		}
		
		// insert new singers
		String musicName = newMusic.getMusicName();
		Music dummMusic = new Music(musicName);
		dummMusic.setSingerList(newSingerList);
		MusicAlbum dummyMusicAlbum = new MusicAlbum(albumName, year, null);
		insertIntoMusicSinger(dummyMusicAlbum, dummMusic);
		
		// update the updated singers
		for (int i = 0; i < updatedSingerList.size(); i++) {
			Person updateFromSinger = updatedSingerList.get(i);
			Integer updateFromSingerID = tryToFindPerson(updateFromSinger);
			Person updateToSinger = newMusic.getSingerList().get(i);
			
			updatePersonWithID(updateFromSingerID, updateToSinger);
		}
	}
	
	/**
	 * Compare and update PeopleInvolvedMusic Table
	 * @param oldMusicAlbum
	 * @param newMusicAlbum
	 * @throws SQLException 
	 */
	private static void compareAndUpdatePeopleInvolvedMusicTable(MusicAlbum oldMusicAlbum, MusicAlbum newMusicAlbum) throws SQLException {
		HashMap<String, ArrayList<Music>> sameRemovedNewMap = determineRemovedUpdatedAndNewMusicTrack(oldMusicAlbum.getMusicTrackList(), newMusicAlbum.getMusicTrackList());
		ArrayList<Music> updatedMusicList = sameRemovedNewMap.get("updated");
		ArrayList<Music> removedMusicList = sameRemovedNewMap.get("removed");
		ArrayList<Music> newMusicList = sameRemovedNewMap.get("new");
		
		// Remove the song writer, composer and arranger in the removedMusicList
		String albumName = oldMusicAlbum.getAlbumName();
		Integer year = oldMusicAlbum.getYearPublished();
		for (Music music : removedMusicList) {
			String musicName = music.getMusicName();
			removeAMusicFromPeopleInvolvedMusicTable(albumName, year, musicName);
		}
		
		// Update each song writer, composer and arranger in the updatedMusicList
		for (int i = 0; i < updatedMusicList.size(); i++) {
			Music oldMusic = updatedMusicList.get(i);
			Person oldComposer = oldMusic.getComposer();
			Person oldArranger = oldMusic.getArranger();
			Person oldSongWriter = oldMusic.getSongWriter();
			
			Music newMusic = newMusicAlbum.getMusicTrackList().get(i);
			Person newComposer = newMusic.getComposer();
			Person newArranger = newMusic.getArranger();
			Person newSongWriter = newMusic.getSongWriter();
			
			// update composer
			int oldComposerID = tryToFindPerson(oldComposer);
			updatePersonWithID(oldComposerID, newComposer);
			
			// update arranger
			int oldArrangerID = tryToFindPerson(oldArranger);
			updatePersonWithID(oldArrangerID, newArranger);
			
			// update song writer
			int oldSongWriterID = tryToFindPerson(oldSongWriter);
			updatePersonWithID(oldSongWriterID, newSongWriter);
		}
		
		// Add new composer, arranger, and song writer
		for (Music music : newMusicList) {
			insertIntoPeopleInvolvedMusic(oldMusicAlbum, music);
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
	private static void removeARowFromMusicTable(String albumName, Integer year, String musicName) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "DELETE FROM " + MusicTable.TABLE_NAME + " "
					+ "WHERE " + MusicTable.ALBUM_NAME + " = ? "
					+ "and " + MusicTable.YEAR + " = ? "
					+ "and " + MusicTable.MUSIC_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, albumName);
			ps.setInt(2, year);
			ps.setString(3, musicName);
			ps.executeUpdate();
			
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
	private static void removeMusicFromMusicSingerTable(String albumName, Integer year, String musicName) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "DELETE FROM " + MusicSingerTable.TABLE_NAME + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = ? "
					+ "and " + MusicSingerTable.YEAR + " = ? "
					+ "and " + MusicSingerTable.MUSIC_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, albumName);
			ps.setInt(2, year);
			ps.setString(3, musicName);
			ps.executeUpdate();
			
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Remove a row from people involved music table
	 * @param albumName with no ''
	 * @param year
	 * @param musicName with no ''
	 * @throws SQLException 
	 */
	private static void removeAMusicFromPeopleInvolvedMusicTable(String albumName, Integer year, String musicName) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "DELETE FROM " + PeopleInvolvedMusicTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedMusicTable.ALBUM_NAME + " = ? "
					+ "and " + PeopleInvolvedMusicTable.YEAR + " = ? "
					+ "and " + PeopleInvolvedMusicTable.MUSIC_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, albumName);
			ps.setInt(2, year);
			ps.setString(3, musicName);
			ps.executeUpdate();
			
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
	private static void removeARowFromMusicSingerTable(String albumName, Integer year, String musicName, Integer singerID) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, sqlUsername, sqlPassword)) {
			
			String query = "DELETE FROM " + MusicSingerTable.TABLE_NAME + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = ? "
					+ "and " + MusicSingerTable.YEAR + " = ? "
					+ "and " + MusicSingerTable.MUSIC_NAME + " = ? "
					+ "and " + MusicSingerTable.PEOPLE_INVOLVED_ID + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, albumName);
			ps.setInt(2, year);
			ps.setString(3, musicName);
			ps.setInt(4, singerID);
			ps.executeUpdate();
			
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
			
			String query = "DELETE FROM " + MusicSingerTable.TABLE_NAME + " "
					+ "WHERE " + MusicSingerTable.ALBUM_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, oldMusicAlbum.getAlbumName());
			ps.executeUpdate();
			
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
			
			String query = "DELETE FROM " + MusicTable.TABLE_NAME + " "
					+ "WHERE " + MusicTable.ALBUM_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, oldMusicAlbum.getAlbumName());
			ps.executeUpdate();
			
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
			
			String query = "DELETE FROM " + PeopleInvolvedTable.TABLE_NAME + " "
					+ "WHERE " + PeopleInvolvedMusicTable.ALBUM_NAME + " = ?";
			
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, oldMusicAlbum.getAlbumName());
			ps.executeUpdate();
			
			connection.close();
			
		} catch (SQLException e) {
		    throw new SQLException(e);
		}
	}
}
