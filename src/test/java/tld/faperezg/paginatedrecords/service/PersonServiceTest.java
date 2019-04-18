package tld.faperezg.paginatedrecords.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import tld.faperezg.paginatedrecords.entity.Person;
import tld.faperezg.paginatedrecords.repository.PersonRepository;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@RunWith (SpringRunner.class)
@DataJpaTest
public class PersonServiceTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PersonRepository personRepository;

	private PersonService personService;

	@Before
	public void setUp () {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory ();
		Validator validator = factory.getValidator ();
		this.personService = new PersonService (this.personRepository, validator);

		this.entityManager.clear ();

		for (int i = 0; i < 25; i++) {
			Person person = new Person ();
			person.setAge ((byte) (i + 20))
				.setFirstName ("First name " + i)
				.setGender (i % 2 == 0 ? "M" : "F")
				.setHomeAddress ("Address " + i)
				.setLastName ("Last name " + i)
				.setPassportId ("V" + i);
			this.entityManager.persist (person);
		}
	}

	public void testCreate (Person person) throws PersonServiceException {
		Assert.assertNull (person.getId ());
		this.personService.create (person);
		Assert.assertNotNull (person.getId ());

		Person dbPerson = this.entityManager.find (Person.class, person.getId ());
		Assert.assertNotNull (dbPerson);
		Assert.assertEquals (dbPerson.getId (), person.getId ());
		Assert.assertEquals (43, dbPerson.getAge ());
		Assert.assertEquals ("Mary", dbPerson.getFirstName ());
		Assert.assertEquals ("F", dbPerson.getGender ());
		Assert.assertEquals ("Somewhere in the world", dbPerson.getHomeAddress ());
		Assert.assertEquals ("Jane", dbPerson.getLastName ());
		Assert.assertEquals ("C571932584", dbPerson.getPassportId ());
	}

	public void testUpdate (Person person) throws PersonServiceException {
		person.setAge (45)
			.setFirstName ("John")
			.setGender ("M")
			.setHomeAddress ("Do not know")
			.setLastName ("Doe")
			.setPassportId ("U3571598246");
		this.personService.update (person);

		Person dbPerson = this.entityManager.find (Person.class, person.getId ());
		Assert.assertNotNull (dbPerson);
		Assert.assertEquals (dbPerson.getId (), person.getId ());
		Assert.assertEquals (45, dbPerson.getAge ());
		Assert.assertEquals ("John", dbPerson.getFirstName ());
		Assert.assertEquals ("M", dbPerson.getGender ());
		Assert.assertEquals ("Do not know", dbPerson.getHomeAddress ());
		Assert.assertEquals ("Doe", dbPerson.getLastName ());
		Assert.assertEquals ("U3571598246", dbPerson.getPassportId ());
	}

	public void testDelete (Person person) {
		this.personService.delete (person);
		Person dbPerson = this.entityManager.find (Person.class, person.getId ());
		Assert.assertNull (dbPerson);
	}

	@Test
	public void testCrud () {
		try {
			Person person = new Person ();
			person.setAge (43)
				.setFirstName ("Mary")
				.setGender ("F")
				.setHomeAddress ("Somewhere in the world")
				.setLastName ("Jane")
				.setPassportId ("C571932584");

			this.testCreate (person);
			this.testUpdate (person);
			this.testDelete (person);
		} catch (PersonServiceException pse) {
			Assert.fail ("Se ha recibido una excepción inesperada: " + pse.getMessage ());
		}
	}


}
