package items;

import java.util.ArrayList;

/**
 * @author xiewen4
 * This object is used to represent a Book object
 */
public class Book {
	private String bookName;
	private String bookISBN;
	private String publisherName;
	private int editionNumber;
	private ArrayList<Person> authorList;
	private int numOfPage;
	private int publicationYear;
	private ArrayList<String> keyWords;
	private String bookDescription;
	
	public Book(String bookISBN, String bookName, String publisher, int numOfPage, int publicationYear) {
		this.bookISBN = bookISBN;
		this.bookName = bookName;
		this.publisherName = publisher;
		this.numOfPage = numOfPage;
		this.publicationYear = publicationYear;
		this.authorList = new ArrayList<>();
		this.keyWords = new ArrayList<>();
		this.editionNumber = -1;
	}

	/**
	 * @return the bookName
	 */
	public String getBookName() {
		String result = "NULL";
		if (bookName != null) {
			result = "'" + this.bookName + "'";
		}
		return result;
	}

	/**
	 * @param bookName the bookName to set
	 */
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	/**
	 * @return the bookISBN
	 */
	public String getBookISBN() {
		String result = "NULL";
		if (this.bookISBN != null) {
			result = "'" + this.bookISBN + "'";
		}
		return result;
	}

	/**
	 * @param bookISBN the bookISBN to set
	 */
	public void setBookISBN(String bookISBN) {
		this.bookISBN = bookISBN;
	}

	/**
	 * @return the publisherName
	 */
	public String getPublisherName() {
		String result = "NULL";
		if (this.publisherName != null) {
			result = "'" + this.publisherName + "'";
		}
		return result;
	}

	/**
	 * @param publisherName the publisherName to set
	 */
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	/**
	 * @return the editionNumber
	 */
	public String getEditionNumber() {
		String result = "NULL";
		if (this.editionNumber != -1) {
			result = "" + this.editionNumber;
		}
		return result;
	}

	/**
	 * @param editionNumber the editionNumber to set
	 */
	public void setEditionNumber(int editionNumber) {
		this.editionNumber = editionNumber;
	}

	/**
	 * @return the authorList
	 */
	public ArrayList<Person> getAuthorList() {
		return authorList;
	}

	/**
	 * @param authorList the authorList to set
	 */
	public void setAuthorList(ArrayList<Person> authorList) {
		this.authorList = authorList;
	}
	
	public void addAuthor(Person newAuthor) {
		this.authorList.add(newAuthor);
	}

	/**
	 * @return the numOfPage
	 */
	public String getNumOfPage() {
		String result = "NULL";
		if (this.numOfPage != -1)
			result = "" + this.numOfPage;
		return result;
	}

	/**
	 * @param numOfPage the numOfPage to set
	 */
	public void setNumOfPage(int numOfPage) {
		this.numOfPage = numOfPage;
	}

	/**
	 * @return the publicationYear
	 */
	public String getPublicationYear() {
		String result = "NULL";
		if (this.numOfPage != -1)
			result = "" + this.publicationYear;
		return result;
	}

	/**
	 * @param publicationYear the publicationYear to set
	 */
	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	/**
	 * @return the keyWords
	 */
	public ArrayList<String> getKeyWords() {
		return keyWords;
	}

	/**
	 * @param keyWords the keyWords to set
	 */
	public void setKeyWords(ArrayList<String> keyWords) {
		this.keyWords = keyWords;
	}
	
	public void addKeyWord(String keyWord) {
		this.keyWords.add(keyWord);
	}

	/**
	 * @return the bookDescription
	 */
	public String getBookDescription() {
		String result = "NULL";
		if (this.bookDescription != null) {
			result = "'" + this.bookDescription + "'";
		}
		return result;
	}

	/**
	 * @param bookDescription the bookDescription to set
	 */
	public void setBookDescription(String bookDescription) {
		this.bookDescription = bookDescription;
	}
}
