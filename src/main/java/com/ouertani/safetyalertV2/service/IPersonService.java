package com.ouertani.safetyalertV2.service;

import java.util.List;

import com.ouertani.safetyalertV2.model.Person;

public interface IPersonService {

	
	Person addPerson(Person person);
	
	Person putPerson(Person person);

	boolean deletePerson(String firstName, String lastName);
	
	// ---
	
	List<Person> getPersonAdress(String Adress);
	
	Person getPerson(String firstName, String lastName);
	
	List<Person> getPersonCity(String city);

	// ---

	//Optional<Person> getPerson(long id);

	//Iterable<Person> getPersons();

}
