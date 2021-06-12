package com.ouertani.safetyalertV2.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.Person;

public interface IPersonService {

	Person addPerson(Person person) throws JsonGenerationException, JsonMappingException, IOException;

	Person putPerson(Person person) throws JsonGenerationException, JsonMappingException, IOException;

	Person deletePerson(String firstName, String lastName)
			throws JsonGenerationException, JsonMappingException, IOException;

	// ---

	List<Person> getPersonAdress(String idAdress) throws JsonGenerationException, JsonMappingException, IOException;

	Person getPerson(String firstName, String lastName)
			throws JsonGenerationException, JsonMappingException, IOException;

	List<Person> getPersonCity(String city) throws JsonGenerationException, JsonMappingException, IOException;

	// ---

	// Optional<Person> getPerson(long id);

	// Iterable<Person> getPersons();

}
