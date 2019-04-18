package tld.faperezg.paginatedrecords.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table (name = "people", indexes = { @Index (name = "idx_person_age", columnList = "age") })
public class Person {
	@Id
	@GeneratedValue
	private Long id;

	@Column (name = "age")
	private byte age;

	@Column (name = "first_name", length = 100)
	@NotEmpty (message = "Debes suministrar el nombre")
	@Size (max = 100, message = "El nombre es demasiado largo (máximo 100 caracteres)")
	private String firstName;

	@Column (name = "gender", length = 1)
	@NotEmpty (message = "Debes suministrar el género")
	@Pattern (regexp = "^M|m|F|f$", message = "El género debe ser M ó F")
	private String gender;

	@Column (name = "home_address")
	private String homeAddress;

	@Column (name = "last_name", length = 100)
	@NotEmpty (message = "Debes suministrar el apellido")
	@Size (max = 100, message = "El apellido es demasiado largo (máximo 100 caracteres)")
	private String lastName;

	@Column (name = "passport_id", length = 25)
	@NotEmpty (message = "Debes suministrar el identificador del pasaporte")
	@Size (max = 25, message = "El identificador del pasaporte es demasiado largo (máximo 25 caracteres)")
	private String passportId;

	public Person () {
		super ();
	}

	public Long getId () {
		return id;
	}

	public byte getAge () {
		return age;
	}

	public String getFirstName () {
		return firstName;
	}

	public String getGender () {
		return gender;
	}

	public String getHomeAddress () {
		return homeAddress;
	}

	public String getLastName () {
		return lastName;
	}

	public String getPassportId () {
		return passportId;
	}

	public Person setId (Long id) {
		this.id = id;
		return this;
	}

	public Person setAge (byte age) {
		this.age = age;
		return this;
	}

	public Person setAge (int age) {
		this.age = (byte) age;
		return this;
	}

	public Person setFirstName (String firstName) {
		this.firstName = firstName;
		return this;
	}

	public Person setGender (String gender) {
		if (gender != null) {
			this.gender = gender.toUpperCase ();
		} else {
			this.gender = null;
		}
		return this;
	}

	public Person setHomeAddress (String homeAddress) {
		this.homeAddress = homeAddress;
		return this;
	}

	public Person setLastName (String lastName) {
		this.lastName = lastName;
		return this;
	}

	public Person setPassportId (String passportId) {
		this.passportId = passportId;
		return this;
	}

	@Override
	public String toString () {
		return "Person{" +
			"id=" + id +
			", age=" + age +
			", firstName='" + firstName + '\'' +
			", gender=" + gender +
			", homeAddress='" + homeAddress + '\'' +
			", lastName='" + lastName + '\'' +
			", passportId='" + passportId + '\'' +
			'}';
	}

	/**
	 * Método para facilitar encadenamiento de métodos
	 *
	 * @return Person Una nueva instancia de la clase Person
	 */
	public static Person getInstance () {
		return new Person ();
	}
}
