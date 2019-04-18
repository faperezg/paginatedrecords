package tld.faperezg.paginatedrecords.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tld.faperezg.paginatedrecords.entity.Person;
import tld.faperezg.paginatedrecords.repository.PersonRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

/**
 * Se encarga de gestionar las operaciones CRUD sobre el objeto Person
 */
@Service
@Transactional (readOnly = true)
public class PersonService {

	/**
	 * Repositorio de objetos Person
	 */
	private final PersonRepository repository;

	/**
	 * Servicio validador
	 */
	private final Validator validator;

	/**
	 * Constructor
	 *
	 * @param repository PersonRepository Repositorio de objetos Person
	 * @param validator  Validator Servicio validador
	 */
	public PersonService (PersonRepository repository, Validator validator) {
		this.repository = repository;
		this.validator = validator;
	}

	/**
	 * @param person Person La persona a validar
	 * @return Set<ConstraintViolation < T>> El conjunto de violaciones encontradas (si las hay)
	 */
	private Set<ConstraintViolation<Person>> validate (Person person) {
		return validator.validate (person);
	}

	/**
	 * Crea una nueva persona en la base de datos
	 *
	 * @param person Person La persona a crear
	 * @return Person La persona guardada
	 * @throws PersonServiceException Si se presenta algún error de validación de la persona suministrada
	 */
	@Transactional
	public Person create (Person person) throws PersonServiceException {
		Set<ConstraintViolation<Person>> errors = this.validate (person);
		if (!errors.isEmpty ()) {
			ConstraintViolationException e = new ConstraintViolationException (this.validate (person));
			throw new PersonServiceException ("Se han presentado errores al crear la persona suministrada", e);
		}

		return this.repository.saveAndFlush (person);
	}

	/**
	 * Elimina una persona existente en la base de datos si la persona suministrada existe
	 *
	 * @param person Person La persona a eliminar
	 */
	public void delete (Person person) {
		Optional<Person> dummy = this.repository.findById (person.getId ());
		if (dummy.isPresent ()) {
			this.repository.delete (person);
		}
	}

	/**
	 * Obtiene una página de personas
	 *
	 * @param pageNumber int Número de la página que se desea obtener
	 * @param size int Número de registros por página
	 * @return Page La página solicitada
	 */
	public Page<Person> fetchAll (int pageNumber, int size) {
		return this.repository.findAll (PageRequest.of ((pageNumber - 1), size));
	}

	/**
	 * Obtiene una persona con el ID suministrado
	 *
	 * @param id Long El ID de la persona a obtener
	 * @return Person|null El objeto persona correspondiente al ID suministrado, o null si no está registrado
	 */
	public Person fetchOne (Long id) {
		Optional<Person> person = this.repository.findById (id);
		return person.orElse (null);
	}

	/**
	 * Actualiza una persona existente en la base de datos
	 *
	 * @param person Person La persona a actualizar
	 * @return Person La persona actualizada
	 * @throws PersonServiceException Si la persona suministrada no se encuentra registrada o se presenta algún error de validación
	 */
	public Person update (Person person) throws PersonServiceException {
		Optional<Person> dummy = this.repository.findById (person.getId ());
		if (!dummy.isPresent ()) {
			throw new PersonServiceException ("No se encuentra registrada la persona a actualizar");
		}

		Set<ConstraintViolation<Person>> errors = this.validate (person);
		if (!errors.isEmpty ()) {
			ConstraintViolationException e = new ConstraintViolationException (this.validate (person));
			throw new PersonServiceException ("Se han presentado errores al actualizar la persona suministrada", e);
		}

		return this.repository.saveAndFlush (person);
	}

}
