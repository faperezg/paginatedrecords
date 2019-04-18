package tld.faperezg.paginatedrecords.resource;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tld.faperezg.paginatedrecords.entity.Person;
import tld.faperezg.paginatedrecords.service.PersonService;
import tld.faperezg.paginatedrecords.service.PersonServiceException;
import tld.faperezg.paginatedrecords.virtual.PersonData;
import tld.faperezg.paginatedrecords.virtual.PersonDataCollection;

/**
 * Servicio web para la gestión de personas
 */
@RestController
@RequestMapping ("/api/people")
public class PersonResource {

	private final PersonService personService;

	/**
	 * Constructor
	 *
	 * @param personService PersonService El servicio gestor de operaciones sobre el objeto Person
	 */
	public PersonResource (PersonService personService) {
		this.personService = personService;
	}

	@PostMapping
	public ResponseEntity<Person> create (@RequestBody PersonData personData) throws PersonServiceException {
		Person person = Person.getInstance ()
			.setAge (personData.getAge ())
			.setFirstName (personData.getFirstName ())
			.setLastName (personData.getLastName ())
			.setGender (personData.getGender ())
			.setHomeAddress (personData.getHomeAddress ())
			.setPassportId (personData.getPassportId ());

		return new ResponseEntity<> (this.personService.create (person), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity fetchAll (@RequestParam (name = "page", defaultValue = "1", required = false) int page, @RequestParam (name = "size", defaultValue = "15", required = false) int size) {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
		String currentUrl = builder.toUriString ();

		Page<Person> peoplePage = this.personService.fetchAll (page, size);
		PersonDataCollection collection = new PersonDataCollection (peoplePage, currentUrl, size);
		return new ResponseEntity<> (collection.getContent (), HttpStatus.OK);
	}

	@PutMapping ("/{id}")
	public ResponseEntity<Person> update (@PathVariable ("id") Long id, @RequestBody PersonData personData) throws PersonServiceException {
		Person dummy = this.personService.fetchOne (id);
		if (dummy == null) {
			return new ResponseEntity<> (HttpStatus.NOT_FOUND);
		}

		Person person = Person.getInstance ()
			.setAge (personData.getAge ())
			.setFirstName (personData.getFirstName ())
			.setLastName (personData.getLastName ())
			.setGender (personData.getGender ())
			.setHomeAddress (personData.getHomeAddress ())
			.setPassportId (personData.getPassportId ());

		return new ResponseEntity<> (this.personService.update (person), HttpStatus.OK);
	}

	@DeleteMapping ("/{id}")
	public ResponseEntity<Person> delete (@PathVariable ("id") Long id) {
		Person person = this.personService.fetchOne (id);
		if (person != null) {
			this.personService.delete (person);
		}
		return new ResponseEntity<> (HttpStatus.OK);
	}
}
