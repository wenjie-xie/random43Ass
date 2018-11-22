package database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import database.tables.BookTable;
import database.tables.KeywordTable;
import database.tables.PeopleInvolvedTable;
import items.Book;
import items.Music;
import items.MusicAlbum;
import items.Person;

class DatabaseConnectionBookApiTest {
	protected static final String URL = "jdbc:mysql://localhost:3306/HL?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	protected static final String sqlUsername = "root";
	protected static final String sqlPassword = "a";

	@Test
	public void testMusicAlbumInsert() throws SQLException {
		
		String nameOfDisk = "Name of Disk";
		Integer diskType = 1;
		Integer yearPublished = 2000;
		Person producer = new Person("P S", "P FN");
		producer.setMiddleName("P MN");
		producer.setGender(1);
		
		Person singer1 = new Person("S1 S", "S1 FN");
		singer1.setMiddleName("S1 MN");
		
		Person singer2 = new Person("S2 S", "S2 FN");
		
		Person songWriter = new Person("SW S", null);
		
		Person composer = new Person("C S", null);
		composer.setMiddleName("C MN");
		composer.setGender(1);
		
		Person arranger = new Person(null, "A FN");
		arranger.setMiddleName("A MN");
		
		Music music = new Music("Track1");
		music.setLanguage("English");
		music.setSingerList(new ArrayList<>(Arrays.asList(singer1, singer2)));
		music.setSongWriter(songWriter);
		music.setComposer(composer);
		music.setArranger(arranger);
		ArrayList<Music> musicTrackList = new ArrayList<>(Arrays.asList(music));
		
		MusicAlbum musicAlbum = new MusicAlbum("Name of Disk",
				2000, musicTrackList);
		musicAlbum.setDiskType(diskType);
		musicAlbum.setYearPublished(yearPublished);
		musicAlbum.setProducer(producer);
		
		DatabaseConnectionMusicAlbumApi.insertMusicAlbum(musicAlbum);
	}

}
