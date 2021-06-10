package com.ouertani.safetyalertV2.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.Mapping;
import com.ouertani.safetyalertV2.model.Person;
import com.ouertani.safetyalertV2.service.IMappingService;
import com.ouertani.safetyalertV2.service.IPersonService;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
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
	public Person addPerson(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person putPerson(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deletePerson(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return false;
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
	public Person getPerson(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> getPersonCity(String city) {
		// TODO Auto-generated method stub
		return null;
	}

}
