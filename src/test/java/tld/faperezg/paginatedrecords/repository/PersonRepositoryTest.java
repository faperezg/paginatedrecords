package tld.faperezg.paginatedrecords.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import tld.faperezg.paginatedrecords.entity.Person;

@RunWith  (SpringRunner.class)
@DataJpaTest
public class PersonRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void testFindAllFirstPage () {
		PageRequest pageRequest = PageRequest.of (0, 10);
		Page<Person> page = this.personRepository.findAll (pageRequest);
		Assert.assertEquals (40, page.getTotalElements ());
		Assert.assertEquals (10, page.getNumberOfElements ());
		Assert.assertEquals (4, page.getTotalPages ());
	}

	@Test
	public void testFindAllLastPage () {
		PageRequest pageRequest = PageRequest.of (2, 15);
		Page<Person> page = this.personRepository.findAll (pageRequest);
		Assert.assertEquals (40, page.getTotalElements ());
		Assert.assertEquals (10, page.getNumberOfElements ());
		Assert.assertEquals (3, page.getTotalPages ());
	}

}
