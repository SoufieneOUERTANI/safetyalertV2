package com.ouertani.safetyalertV2.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.Person;

public interface IPersonService {

	Person addPerson(Person person);

	Person putPerson(Person person);

	boolean deletePerson(String firstName, String lastName);

	// ---

	List<Person> getPersonAdress(String idAdress) throws JsonGenerationException, JsonMappingException, IOException;

	Person getPerson(String firstName, String lastName);

	List<Person> getPersonCity(String city);

	// ---

	// Optional<Person> getPerson(long id);

	// Iterable<Person> getPersons();

}
