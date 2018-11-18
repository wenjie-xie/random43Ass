/**
 * 
 */
package items;

/**
 * @author xiewen4
 * This object is use to represent a person
 */
public class Person {
	private String surname;
	private String firstName;
	private String middleName;
	private int gender;
	
	public Person(String surname, String firstName) {
		this.firstName = firstName;
		this.surname = surname;
		this.gender = -1;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		String result = "NULL";
		if (this.surname != null) {
			result = "'" + this.surname + "'";
		}
		return result;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		String result = "NULL";
		if (this.firstName != null) {
			result = "'" + this.firstName + "'";
		}
		return result;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		String result = "NULL";
		if (this.middleName != null) {
			result = "'" + this.middleName + "'";
		}
		return result;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the isMale
	 */
	public String getGender() {
		String result = "NULL";
		if (this.gender != -1) {
			result = "" + this.gender;
		}
		return result;
	}

	/**
	 * @param isMale the isMale to set
	 */
	public void setMale(int gender) {
		this.gender = gender;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + gender;
		result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
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
		Person other = (Person) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender != other.gender)
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}
}
