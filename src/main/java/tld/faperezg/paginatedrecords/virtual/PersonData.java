package tld.faperezg.paginatedrecords.virtual;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class PersonData {
	private byte age;

	private String firstName;

	private String gender;

	private String homeAddress;

	private String lastName;

	private String passportId;

	private List<Object> _links;

	public PersonData () {
		this._links = new ArrayList<> ();
	}

	public byte getAge () {
		return this.age;
	}

	public String getFirstName () {
		return this.firstName;
	}

	public String getGender () {
		return this.gender;
	}

	public String getHomeAddress () {
		return this.homeAddress;
	}

	public String getLastName () {
		return this.lastName;
	}

	public List<Object> get_links () {
		return this._links;
	}

	public String getPassportId () {
		return this.passportId;
	}

	public PersonData setAge (byte age) {
		this.age = age;
		return this;
	}

	public PersonData setFirstName (String firstName) {
		this.firstName = firstName;
		return this;
	}

	public PersonData setGender (String gender) {
		if (gender != null) {
			this.gender = gender.toUpperCase ();
		} else {
			this.gender = null;
		}
		return this;
	}

	public PersonData setHomeAddress (String homeAddress) {
		this.homeAddress = homeAddress;
		return this;
	}

	public PersonData setLastName (String lastName) {
		this.lastName = lastName;
		return this;
	}

	public PersonData setPassportId (String passportId) {
		this.passportId = passportId;
		return this;
	}

	public PersonData addLink (String rel, String url) {
		Hashtable<String, String> link = new Hashtable<> ();
		link.put ("rel", rel);
		link.put ("url", url);

		this._links.add (link);
		return this;
	}

	public static PersonData getInstance () {
		return new PersonData ();
	}

}
