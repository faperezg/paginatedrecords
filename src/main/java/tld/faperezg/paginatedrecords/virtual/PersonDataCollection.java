package tld.faperezg.paginatedrecords.virtual;

import org.springframework.data.domain.Page;
import tld.faperezg.paginatedrecords.entity.Person;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class PersonDataCollection {
	private String url;

	private List<Object> items;

	private Page<Person> page;

	private int recordsPerPage;

	public PersonDataCollection (Page<Person> page, String url, int recordsPerPage) {
		List<Person> people = page.getContent ();
		this.items = new ArrayList<> ();
		for (Person person : people) {
			PersonData personData = PersonData.getInstance ()
				.setAge (person.getAge ())
				.setFirstName (person.getFirstName ())
				.setGender (person.getGender ())
				.setHomeAddress (person.getHomeAddress ())
				.setLastName (person.getLastName ())
				.setPassportId (person.getPassportId ())
				.addLink ("self", url + "/" + person.getId ());
			this.url = url;
			this.items.add (personData);
			this.page = page;
			this.recordsPerPage = recordsPerPage;
		}
	}

	public Hashtable<String, Object> getContent () {
		Hashtable<String, Object> metadata = new Hashtable<> ();
		metadata.put ("count", this.items.size ());
		metadata.put ("total", this.page.getTotalElements ());
		metadata.put ("page", (this.page.getNumber () + 1));
		metadata.put ("size", this.recordsPerPage);

		List<Object> links = new ArrayList<> ();

		Hashtable<String, String> self = new Hashtable<> ();
		self.put ("rel", "self");
		self.put ("href", this.url + "?page=" + (this.page.getNumber () + 1) + "&size=" + this.recordsPerPage);
		links.add (self);

		if (this.page.hasNext ()) {
			Hashtable<String, String> next = new Hashtable<> ();
			next.put ("rel", "next");
			next.put ("href", this.url + "?page=" + (this.page.getNumber () + 2) + "&size=" + this.recordsPerPage);
			links.add (next);

			Hashtable<String, String> last = new Hashtable<> ();
			last.put ("rel", "last");
			last.put ("href", this.url + "?page=" + this.page.getTotalPages () + "&size=" + this.recordsPerPage);
			links.add (last);
		}

		if (this.page.hasPrevious ()) {
			Hashtable<String, String> prev = new Hashtable<> ();
			prev.put ("rel", "prev");
			prev.put ("href", this.url + "?page=" + (this.page.getNumber ()) + "&size=" + this.recordsPerPage);
			links.add (prev);

			Hashtable<String, String> first = new Hashtable<> ();
			first.put ("rel", "first");
			first.put ("href", this.url + "?page=1&size=" + this.recordsPerPage);
			links.add (first);
		}


		Hashtable<String, Object> contents = new Hashtable<> ();
		contents.put ("items", this.items);
		contents.put ("_metadata", metadata);
		contents.put ("_links", links);

		return contents;
	}
}
