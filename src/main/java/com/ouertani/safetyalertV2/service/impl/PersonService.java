package com.ouertani.safetyalertV2.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.ID;
import com.ouertani.safetyalertV2.model.Mapping;
import com.ouertani.safetyalertV2.model.Person;
import com.ouertani.safetyalertV2.service.IMappingService;
import com.ouertani.safetyalertV2.service.IPersonService;

import lombok.Data;

@Data
@Service
public class PersonService implements IPersonService {

	@Value("${JSON_FILE}")
	private String JSON_FILE;

	private static final Logger logger = LogManager.getLogger("FireStationService");

	IMappingService mappingService;

	@Autowired
	public PersonService(IMappingService mappingService) {
		this.mappingService = mappingService;
	}

	@Override
	public Person addPerson(Person person) throws JsonGenerationException, JsonMappingException, IOException {
		logger.info(
				"Paramètre body : " + " person : " + person);

		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		logger.debug("tempMapping : " + tempMapping);

		List<ID> mappingPersonID = tempMapping.getPersons().stream()
				.map(c -> new ID(c.getFirstName(), c.getLastName())).collect(Collectors.toList());

		logger.debug("mappingPersonID : " + mappingPersonID);

		ID thePersonID = new ID(person.getFirstName(), person.getLastName());

		if (!mappingPersonID.contains(thePersonID)) {
			tempMapping.getPersons().add(person);
		} else {
			tempMapping.getFirestations().toString();
			logger.warn("L'objet existe déjà");
			logger.debug("person : " + person);
			logger.debug("mapping.getPersons().toString() : " + tempMapping.getPersons().toString());

			throw new ResponseStatusException(HttpStatus.CONFLICT, "Custom Error : L'objet existe déjà");
		}
		mappingService.writeJsonFile(JSON_FILE, tempMapping);
		return person;

	}

	@Override
	public Person putPerson(Person person) throws JsonGenerationException, JsonMappingException, IOException {
		logger.info(
				"Paramètre body : " + " person : " + person);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		int tempIndexOf = -1;
		for (Person tempPerson : tempMapping.getPersons()) {
			if (tempPerson.getFirstName().equals(person.getFirstName())
					&& tempPerson.getLastName().equals(person.getLastName())) {
				tempIndexOf = tempMapping.getPersons().indexOf(tempPerson);
				break;
			}
		}

		if (tempIndexOf > -1) {
			tempMapping.getPersons().get(tempIndexOf).setAddress(person.getAddress());
			tempMapping.getPersons().get(tempIndexOf).setCity(person.getCity());
			tempMapping.getPersons().get(tempIndexOf).setEmail(person.getEmail());
			tempMapping.getPersons().get(tempIndexOf).setPhone(person.getPhone());
			tempMapping.getPersons().get(tempIndexOf).setZip(person.getZip());
			mappingService.writeJsonFile(JSON_FILE, tempMapping);
			return tempMapping.getPersons().get(tempIndexOf);
		}
		return null;
	}

	@Override
	public Person deletePerson(String firstName, String lastName)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info(
				"Paramètre : " + " firstName : " + firstName + "/ lastName : " + lastName);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		Person thePerson = null;
		for (Person tempPerson : tempMapping.getPersons()) {
			if (tempPerson.getFirstName().equals(firstName) && tempPerson.getLastName().equals(lastName)) {
				thePerson = tempPerson;
				break;
			}
		}
		if (thePerson != null) {
			tempMapping.getPersons().remove(thePerson);
			mappingService.writeJsonFile(JSON_FILE, tempMapping);
			return thePerson;
		}
		return null;
	}

	@Override
	public List<Person> getPersonAdress(String idAdress)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("Paramètre " + "/ idAdress : " + idAdress);
		List<Person> tempReturn = new ArrayList<Person>();
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		logger.debug("tempMapping : " + tempMapping);
		for (Person tempPerson : tempMapping.getPersons()) {
			logger.debug("tempPerson : " + tempPerson);
			if (tempPerson.getAddress().equals(idAdress)) {
				tempReturn.add(tempPerson);
				logger.debug("tempPerson : " + tempPerson);
			}
		}
		logger.info("Retruned List<Person> getPersonAdress : " + tempReturn);
		return tempReturn;
	}

	@Override
	public List<Person> getPerson(String firstName, String lastName)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info(
				"Paramètre : " + " firstName : " + firstName + "/ lastName : " + lastName);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		List<Person> thePerson = new ArrayList<Person>();
		for (Person tempPerson : tempMapping.getPersons()) {
			if (tempPerson.getLastName().equals(lastName) && firstName == null) {
				thePerson.add(tempPerson);
				continue;
			}
			if (tempPerson.getFirstName().equals(firstName) && tempPerson.getLastName().equals(lastName)) {
				thePerson.add(tempPerson);
				continue;
			}
		}
		return thePerson;
	}

	@Override
	public List<Person> getPersonCity(String theCity)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info(
				"Paramètre : " + " theCity : " + theCity);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		List<Person> persons = new ArrayList<Person>();
		for (Person tempPerson : tempMapping.getPersons()) {
			if (tempPerson.getCity().equals(theCity)) {
				persons.add(tempPerson);
			}
		}
		return persons;
	}

}
