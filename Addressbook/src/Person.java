public class Person {
	private String firstName;
	private String lastName;
	private long number;
	private String address;
	private String city;
	private int zipcode;

	public Person(String firstName, String lastName, long number,
			String address, String city, int zipcode) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.number = number;
		this.address = address;
		this.city = city;
		this.zipcode = zipcode;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public long getNumber() {
		return number;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public int getZipcode() {
		return zipcode;
	}

	/*
	 * @Override public int hashCode() { return lastName.hashCode(); }
	 * 
	 * @Override public boolean equals(Object people) { Person person = (Person)
	 * people; return getLastName().equals(person.getLastName()); }
	 * 
	 * @Override public int compareTo(Person people) { return
	 * getLastName().compareTo(people.getLastName()); }
	 */
	@Override
	public String toString() {
		return ("first name: " + firstName + " last name: " + lastName
				+ " number: " + number + " address: " + address + " city: "
				+ city + " zip: " + zipcode);
	}

}
