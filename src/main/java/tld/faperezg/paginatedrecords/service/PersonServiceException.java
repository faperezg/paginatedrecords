package tld.faperezg.paginatedrecords.service;

public class PersonServiceException extends Exception {

	public PersonServiceException (String message) {
		super (message);
	}

	public PersonServiceException (String message, Exception e) {
		super (message, e);
	}
}
