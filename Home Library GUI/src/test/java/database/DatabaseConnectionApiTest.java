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

class DatabaseConnectionApiTest {
	protected static final String URL = "jdbc:mysql://localhost:3306/HL?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	protected static final String sqlUsername = "root";
	protected static final String sqlPassword = "a";

	@Test
	public void testMusicAlbumInsert() {
		
		String nameOfDisk = "Name of Disk";
		Integer diskType = 1;
		Integer yearPublished = 2000;
		Person producer = new Person("P S", "P FN");
		producer.setMiddleName("P MN");
		producer.setGender(1);
		
		// Music 1
		Person singer1 = new Person("S1 S", "S1 FN");
		singer1.setMiddleName("S1 MN");
		
		Person songWriter = new Person("SW S", "SW FN");
		
		Person composer = new Person("C S", "C FN");
		composer.setMiddleName("C MN");
		composer.setGender(1);
		
		Person arranger = new Person("A S", "A FN");
		arranger.setMiddleName("A MN");
		
		Music music = new Music("Track1");
		music.setLanguage("English");
		music.setSingerList(new ArrayList<>(Arrays.asList(singer1)));
		music.setSongWriter(songWriter);
		music.setComposer(composer);
		music.setArranger(arranger);
		
		
		// Music 2
		singer1 = new Person("s1 S", "s1 FN");
		singer1.setMiddleName("s1 MN");
		singer1.setGender(0);
		
		Person singer2 = new Person("s2 S", "s2 FN");
		singer2.setMiddleName("s2 MN");
		singer2.setGender(1);
		
		songWriter = new Person("SW S", "SW FN");
		songWriter.setMiddleName("sw MN");
		songWriter.setGender(2);
		
		composer = new Person("C S", "C FN");
		composer.setMiddleName("C MN");
		composer.setGender(1);
		
		arranger = new Person("A S", "A FN");
		arranger.setMiddleName("A MN");
		arranger.setGender(0);
		
		Music music2 = new Music("Track2");
		music2.setLanguage("French");
		music2.setSingerList(new ArrayList<>(Arrays.asList(singer1, singer2)));
		music2.setSongWriter(songWriter);
		music2.setComposer(composer);
		music2.setArranger(arranger);
				
				
		ArrayList<Music> musicTrackList = new ArrayList<>(Arrays.asList(music, music2));
		
		MusicAlbum musicAlbum = new MusicAlbum("Name of Disk",
				2000, musicTrackList);
		musicAlbum.setDiskType(diskType);
		musicAlbum.setYearPublished(yearPublished);
		musicAlbum.setProducer(producer);
		
		try {
			DatabaseConnectionMusicAlbumApi.insertMusicAlbum(musicAlbum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
