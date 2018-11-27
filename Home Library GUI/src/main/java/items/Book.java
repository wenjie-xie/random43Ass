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
	private Integer editionNumber;
	private ArrayList<Person> authorList;
	private Integer numOfPage;
	private Integer publicationYear;
	private ArrayList<String> keyWords;
	private String bookDescription;
	
	public Book(String bookISBN, String bookName, String publisher, Integer numOfPage, Integer publicationYear) {
		this.bookISBN = bookISBN;
		this.bookName = bookName;
		this.publisherName = publisher;
		this.numOfPage = numOfPage;
		this.publicationYear = publicationYear;
		this.authorList = new ArrayList<>();
		this.keyWords = new ArrayList<>();
		this.editionNumber = null;
	}

	/**
	 * @return the bookName
	 */
	public String getBookName() {
		return bookName;
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
		return bookISBN;
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
		return publisherName;
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
	public Integer getEditionNumber() {
		return editionNumber;
	}

	/**
	 * @param editionNumber the editionNumber to set
	 */
	public void setEditionNumber(Integer editionNumber) {
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

	/**
	 * @return the numOfPage
	 */
	public Integer getNumOfPage() {
		return numOfPage;
	}

	/**
	 * @param numOfPage the numOfPage to set
	 */
	public void setNumOfPage(Integer numOfPage) {
		this.numOfPage = numOfPage;
	}

	/**
	 * @return the publicationYear
	 */
	public Integer getPublicationYear() {
		return publicationYear;
	}

	/**
	 * @param publicationYear the publicationYear to set
	 */
	public void setPublicationYear(Integer publicationYear) {
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

	/**
	 * @return the bookDescription
	 */
	public String getBookDescription() {
		return bookDescription;
	}

	/**
	 * @param bookDescription the bookDescription to set
	 */
	public void setBookDescription(String bookDescription) {
		this.bookDescription = bookDescription;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Book [bookName=" + bookName + ", bookISBN=" + bookISBN + ", publisherName=" + publisherName
				+ ", editionNumber=" + editionNumber + ", authorList=" + authorList + ", numOfPage=" + numOfPage
				+ ", publicationYear=" + publicationYear + ", keyWords=" + keyWords + ", bookDescription="
				+ bookDescription + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorList == null) ? 0 : authorList.hashCode());
		result = prime * result + ((bookDescription == null) ? 0 : bookDescription.hashCode());
		result = prime * result + ((bookISBN == null) ? 0 : bookISBN.hashCode());
		result = prime * result + ((bookName == null) ? 0 : bookName.hashCode());
		result = prime * result + ((editionNumber == null) ? 0 : editionNumber.hashCode());
		result = prime * result + ((keyWords == null) ? 0 : keyWords.hashCode());
		result = prime * result + ((numOfPage == null) ? 0 : numOfPage.hashCode());
		result = prime * result + ((publicationYear == null) ? 0 : publicationYear.hashCode());
		result = prime * result + ((publisherName == null) ? 0 : publisherName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (authorList == null) {
			if (other.authorList != null)
				return false;
		} else if (!authorList.equals(other.authorList))
			return false;
		if (bookDescription == null) {
			if (other.bookDescription != null)
				return false;
		} else if (!bookDescription.equals(other.bookDescription))
			return false;
		if (bookISBN == null) {
			if (other.bookISBN != null)
				return false;
		} else if (!bookISBN.equals(other.bookISBN))
			return false;
		if (bookName == null) {
			if (other.bookName != null)
				return false;
		} else if (!bookName.equals(other.bookName))
			return false;
		if (editionNumber == null) {
			if (other.editionNumber != null)
				return false;
		} else if (!editionNumber.equals(other.editionNumber))
			return false;
		if (keyWords == null) {
			if (other.keyWords != null)
				return false;
		} else if (!keyWords.equals(other.keyWords))
			return false;
		if (numOfPage == null) {
			if (other.numOfPage != null)
				return false;
		} else if (!numOfPage.equals(other.numOfPage))
			return false;
		if (publicationYear == null) {
			if (other.publicationYear != null)
				return false;
		} else if (!publicationYear.equals(other.publicationYear))
			return false;
		if (publisherName == null) {
			if (other.publisherName != null)
				return false;
		} else if (!publisherName.equals(other.publisherName))
			return false;
		return true;
	}

	
}
