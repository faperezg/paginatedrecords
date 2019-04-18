package tld.faperezg.paginatedrecords.entity;

import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class PersonTest {

	private static Validator validator;

	@BeforeClass
	public static void setUpValidator () {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory ();
		validator = factory.getValidator ();
	}

	@Test
	public void testGenderSetter () {
		Person person = new Person ();
		person.setGender ("m");

		Assert.assertEquals ("M", person.getGender ());
	}

	@Test
	public void testEmptyPersonValidations () {
		Person person = new Person ();

		Set<ConstraintViolation<Person>> constraintViolations = PersonTest.validator.validate (person);
		Assert.assertFalse (constraintViolations.isEmpty ());
		Assert.assertEquals (4, constraintViolations.size ());

		for (ConstraintViolation<Person> violation : constraintViolations) {
			NodeImpl node = ((PathImpl) violation.getPropertyPath ()).getLeafNode ();
			String propertyName = node.getName ();
			String message = violation.getMessage ();


			switch (propertyName) {
				case "firstName":
					Assert.assertEquals ("Debes suministrar el nombre", message);
					break;
				case "gender":
					Assert.assertEquals ("Debes suministrar el género", message);
					break;
				case "lastName":
					Assert.assertEquals ("Debes suministrar el apellido", message);
					break;
				case "passportId":
					Assert.assertEquals ("Debes suministrar el identificador del pasaporte", message);
					break;
				default:
					Assert.fail ("Validación no contemplada: " + propertyName + " - " + message);
					break;
			}

		}
	}

	@Test
	public void testFirstNameTooLongValidation () {
		Person person = new Person ();
		person.setAge ((byte) 18)
			.setFirstName ("This is a very long first name, This is a very long first name, This is a very long first name, This is a very long first name")
			.setGender ("m")
			.setLastName ("Doe")
			.setPassportId ("U1234567890");

		Set<ConstraintViolation<Person>> constraintViolations = PersonTest.validator.validate (person);
		Assert.assertFalse (constraintViolations.isEmpty ());
		Assert.assertEquals (1, constraintViolations.size ());

		ConstraintViolation<Person> violation = constraintViolations.iterator ().next ();
		NodeImpl node = ((PathImpl) violation.getPropertyPath ()).getLeafNode ();
		String propertyName = node.getName ();
		String message = violation.getMessage ();
		Assert.assertEquals ("firstName", propertyName);
		Assert.assertEquals ("El nombre es demasiado largo (máximo 100 caracteres)", message);
	}

	@Test
	public void testInvalidGenderValidation () {
		Person person = new Person ();
		person.setAge ((byte) 18)
			.setFirstName ("John")
			.setGender ("X")
			.setLastName ("Doe")
			.setPassportId ("U1234567890");

		Set<ConstraintViolation<Person>> constraintViolations = PersonTest.validator.validate (person);
		Assert.assertFalse (constraintViolations.isEmpty ());
		Assert.assertEquals (1, constraintViolations.size ());

		ConstraintViolation<Person> violation = constraintViolations.iterator ().next ();
		NodeImpl node = ((PathImpl) violation.getPropertyPath ()).getLeafNode ();
		String propertyName = node.getName ();
		String message = violation.getMessage ();
		Assert.assertEquals ("gender", propertyName);
		Assert.assertEquals ("El género debe ser M ó F", message);
	}

	@Test
	public void testLastNameTooLongValidation () {
		Person person = new Person ();
		person.setAge ((byte) 18)
			.setFirstName ("John")
			.setGender ("m")
			.setLastName ("This is a very long last name, This is a very long last name, This is a very long last name, This is a very long last name")
			.setPassportId ("U1234567890");

		Set<ConstraintViolation<Person>> constraintViolations = PersonTest.validator.validate (person);
		Assert.assertFalse (constraintViolations.isEmpty ());
		Assert.assertEquals (1, constraintViolations.size ());

		ConstraintViolation<Person> violation = constraintViolations.iterator ().next ();
		NodeImpl node = ((PathImpl) violation.getPropertyPath ()).getLeafNode ();
		String propertyName = node.getName ();
		String message = violation.getMessage ();
		Assert.assertEquals ("lastName", propertyName);
		Assert.assertEquals ("El apellido es demasiado largo (máximo 100 caracteres)", message);
	}

	@Test
	public void testPassportIdTooLongValidation () {
		Person person = new Person ();
		person.setAge ((byte) 18)
			.setFirstName ("John")
			.setGender ("m")
			.setLastName ("Doe")
			.setPassportId ("U1234567890U1234567890U1234567890");

		Set<ConstraintViolation<Person>> constraintViolations = PersonTest.validator.validate (person);
		Assert.assertFalse (constraintViolations.isEmpty ());
		Assert.assertEquals (1, constraintViolations.size ());

		ConstraintViolation<Person> violation = constraintViolations.iterator ().next ();
		NodeImpl node = ((PathImpl) violation.getPropertyPath ()).getLeafNode ();
		String propertyName = node.getName ();
		String message = violation.getMessage ();
		Assert.assertEquals ("passportId", propertyName);
		Assert.assertEquals ("El identificador del pasaporte es demasiado largo (máximo 25 caracteres)", message);
	}

}
