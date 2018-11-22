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
import items.Movie;
import items.Music;
import items.MusicAlbum;
import items.Person;

class DatabaseConnectionApiTest {
	protected static final String URL = "jdbc:mysql://localhost:3306/HL?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	protected static final String sqlUsername = "root";
	protected static final String sqlPassword = "a";

	
	//@Test
	public void testBookInsert() {
		// Book name
		String bookName = "TestBookName";
		
		// Book ISBN
		String bookISBN = "TestBookISBN";
		
		// Publisher name
		String publisherName = "TestPublisherName";
		
		// Edition Number
		Integer editionNum = 1;
		
		// Author
		Person author1 = new Person("TestAuthor1 S", "TestAuthor1 FN");
		Person author2 = new Person("TestAuthor2 S", "TestAuthor2 FN");
		ArrayList<Person> authorList = new ArrayList<>();
		authorList.add(author1);
		authorList.add(author2);
		
		// Number of page
		Integer numOfPage = 66;
		
		// Publication Year
		Integer publicationYear = 2000;
		
		// Keywords
		ArrayList<String> tags = new ArrayList<>(Arrays.asList("TestTag1", "TestTag2"));
		
		// Book description
		String description = "Test DEscription";
		
		Book book = new Book(bookISBN, bookName, publisherName, numOfPage, publicationYear);
		book.setKeyWords(tags);
		book.setAuthorList(authorList);
		book.setBookDescription(description);
		
		DatabaseConnectionBookApi.insertBook(book);
	}
	
	@Test
	public void testBookUpdate() {
		// Book name
		String bookName = "TestBookName";
		
		// Book ISBN
		String bookISBN = "TestBookISBN";
		
		// Publisher name
		String publisherName = "TestPublisherName";
		
		// Edition Number
		Integer editionNum = 1;
		
		// Author
		Person author1 = new Person("TestAuthor1 S", "TestAuthor1 FN");
		Person author2 = new Person("TestAuthor2 S", "TestAuthor2 FN");
		ArrayList<Person> authorList = new ArrayList<>();
		authorList.add(author1);
		authorList.add(author2);
		
		// Number of page
		Integer numOfPage = 66;
		
		// Publication Year
		Integer publicationYear = 2000;
		
		// Keywords
		ArrayList<String> tags = new ArrayList<>(Arrays.asList("TestTag1", "TestTag2"));
		
		// Book description
		String description = "Test DEscription";
		
		Book book = new Book(bookISBN, bookName, publisherName, numOfPage, publicationYear);
		book.setKeyWords(tags);
		book.setAuthorList(authorList);
		book.setBookDescription(description);
		
		
		// NEW
		
		// Book name
		String newbookName = "newTestBookName";
		
		// Book ISBN
		String newbookISBN = "newBookISBN";
		
		// Publisher name
		String newpublisherName = "newTestPublisherName";
		
		// Edition Number
		Integer neweditionNum = 1;
		
		// Author
		Person newauthor1 = new Person("newTestAuthor1 S", "newTestAuthor1 FN");
		Person newauthor2 = new Person("newTestAuthor2 S", "newTestAuthor2 FN");
		ArrayList<Person> newauthorList = new ArrayList<>();
		authorList.add(newauthor1);
		authorList.add(newauthor2);
		
		// Number of page
		Integer newnumOfPage = 666;
		
		// Publication Year
		Integer newpublicationYear = 20000;
		
		// Keywords
		ArrayList<String> newtags = new ArrayList<>(Arrays.asList("newTestTag1", "newTestTag2"));
		
		// Book description
		String newdescription = "newTest DEscription";
		
		Book newbook = new Book(newbookISBN, newbookName, newpublisherName, newnumOfPage, newpublicationYear);
		book.setKeyWords(newtags);
		book.setAuthorList(newauthorList);
		book.setBookDescription(newdescription);
		
		DatabaseConnectionBookApi.compareAndUpdateBookInfo(book, newbook);
	}
	
	
	//@Test
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
	
	
	//@Test
	public void testMovieInsert() {
		
		// name of the movie
		String movieName = "Testing";
		
		// director
		Person director = new Person("Director S", "Director FN");
		ArrayList<Person> directorList = new ArrayList<>();
		directorList.add(director);
		
		// script writer
		Person scriptWriter = new Person("Script Writer S", "Script Writer FN");
		ArrayList<Person> scriptWriterList = new ArrayList<>();
		scriptWriterList.add(scriptWriter);
		
		// cast
		Person cast1 = new Person("Cast1 S", "Cast1 FN");
		Person cast2 = new Person("Cast2 S", "Cast2 FN");
		ArrayList<Person> castList = new ArrayList<>();
		castList.add(cast1);
		castList.add(cast2);
		
		// producer
		Person producer = new Person("Producer S", "Producer FN");
		ArrayList<Person> producerList = new ArrayList<>();
		producerList.add(producer);
		
		// composer
		Person composer = new Person("Composer S", "Composer FN");
		ArrayList<Person> composerList = new ArrayList<>();
		composerList.add(composer);
		
		// editor
		Person editor = new Person("Editor S", "Editor FN");
		ArrayList<Person> editorList = new ArrayList<>();
		editorList.add(editor);
		
		// costume designer
		Person costumeDesigner = new Person("Costume Designer S", "Costume Designer FN");
		ArrayList<Person> costumeList = new ArrayList<>();
		costumeList.add(costumeDesigner);
		
		// year of release
		Integer releaseYear = 2000;
		
		Movie movie = new Movie(movieName, releaseYear);
		movie.setDirectorList(directorList);
		movie.setScriptWriterList(scriptWriterList);
		movie.setCastList(castList);
		movie.setProducerList(producerList);
		movie.setComposerList(composerList);
		movie.setEditorList(editorList);
		movie.setCostumeDesignerList(costumeList);
		
		DatabaseConnectionMovieApi.insertMovie(movie);
	}

}
