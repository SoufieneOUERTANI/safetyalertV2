package com.ouertani.safetyalertV2.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.dto.MedicalDetailsPersonsAdress;
import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.model.Person;
import com.ouertani.safetyalertV2.service.IFireStationService;
import com.ouertani.safetyalertV2.service.IMedicalRecordService;
import com.ouertani.safetyalertV2.service.IPersonService;
import com.ouertani.safetyalertV2.util.DateCalculator;

@RestController
public class PersonRestController {

	private static final Logger logger = LogManager.getLogger("FireStationRestController");

	@Value("${spring.jackson.date-format}")
	private String DATE_FORMAT;

	private IPersonService personService;
	private IMedicalRecordService medicalRecordService;
	private IFireStationService fireStationService;

	@Autowired
	public PersonRestController(IPersonService thePersonService, IMedicalRecordService theMedicalRecordService,
			IFireStationService theFireStationService) {
		personService = thePersonService;
		medicalRecordService = theMedicalRecordService;
		fireStationService = theFireStationService;

	}

	/*
	 * @GetMapping("/persons") public List<Person> getPersons() { return
	 * (List<Person>) personService.getPersons(); }
	 */

	/*
	 * @GetMapping("/persons/{personId}") public Optional<Person>
	 * getPerson(@PathVariable long personId) {
	 * 
	 * Optional<Person> thePerson = personService.getPerson(personId);
	 * 
	 * if (thePerson == null) { throw new RuntimeException("Person id not found - "
	 * + personId); }
	 * 
	 * return thePerson; }
	 */

	/*
	 * @GetMapping("/") public Mapping global() throws JsonGenerationException,
	 * JsonMappingException, IOException {
	 * 
	 * IMappingService mappingService = new MappingService(); return
	 * FileJsonMapping.mapping = mappingService.readJsonFile();
	 * 
	 * }
	 */

	@PostMapping("/person")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Person addPerson(@RequestBody Person thePerson)
			throws JsonGenerationException, JsonMappingException, IOException {

		Person tempPerson = personService.addPerson(thePerson);
		if (tempPerson == null) {
			throw new RuntimeException(
					"Impossible to add the person - " + thePerson.getFirstName() + " - " + thePerson.getLastName());
		}
		return (tempPerson);
	}

	@PutMapping("/person")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public Person updatePerson(@RequestBody Person thePerson)
			throws JsonGenerationException, JsonMappingException, IOException {

		Person tempPerson = personService.putPerson(thePerson);
		if (tempPerson == null) {
			throw new RuntimeException(
					"Impossible to update the person - " + thePerson.getFirstName() + " - " + thePerson.getLastName());
		}
		return thePerson;
	}

	@DeleteMapping("/person")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public String deletePerson(@RequestParam(defaultValue = "empty") String firstName,
			@RequestParam(defaultValue = "empty") String lastName)
			throws JsonGenerationException, JsonMappingException, IOException {
		if (firstName.equals("empty") || lastName.equals("empty"))
			return "Please give a \"firstName\" and a \"lastName\"";

		Person tempPerson = personService.deletePerson(firstName, lastName);
		if (tempPerson == null) {
			throw new RuntimeException("Person not found - " + firstName + " - " + lastName);
		}
		return "Deleted person id - " + firstName + " - " + lastName;
	}

	/*
	 * ----------------------------------
	 * 
	 * http://localhost:8080/childAlert?address=<address> Cette url doit retourner
	 * une liste d'enfants (tout individu âgé de 18 ans ou moins) habitant à cette
	 * adresse. La liste doit comprendre le prénom et le nom de famille de chaque
	 * enfant, son âge et une liste des autres membres du foyer. S'il n'y a pas
	 * d'enfant, cette url peut renvoyer une chaîne vide.
	 ***
	 * 2 - List<Personne> getPersonAdress(Sting Adress) 3 - MedicalRecord
	 * getMedicalRecordPerson(String firstName, String lastName) 4 - int getAge(Date
	 * BirthDay) => Period.between(birthDate, currentDate).getYears();
	 * 
	 * DTO
	 * 
	 * 
	 * List <Enfant> List <Adulte>
	 */
	/*
	 * @GetMapping("/childAlert") public String
	 * childAlert(@RequestParam(defaultValue = "empty") String address) { if
	 * (firstName == "empty" || lastName == "empty") return
	 * "Please give a \"firstName\" and a \"lastName\"";
	 * 
	 * boolean tempPerson = personService.deletePerson(firstName, lastName); if
	 * (tempPerson == false) { throw new RuntimeException("Person not found - " +
	 * firstName + " - "+ lastName); } return "Deleted person id - " + firstName +
	 * " - "+ lastName; }
	 */

	@GetMapping("/childAlert")
	@ResponseStatus(code = HttpStatus.FOUND)
	public List<Person> getChildrenAdress(
			@RequestParam Map<String, String> allParams)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {

		logger.debug("allParams.size() : " + allParams.size());
		logger.debug("allParams : " + allParams.toString());

		List<Person> chidren = new ArrayList<Person>();
		String adress = allParams.get("address");
		if (allParams.size() == 1 && adress != null) {
			List<Person> tempPersons = personService.getPersonAdress(adress);
			if (tempPersons.size() == 0) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Aucun personne dans cette adresse");
			}

			for (Person tempPerson : tempPersons) {
				MedicalRecord tempMedicalRedord = medicalRecordService.getMedicalRecordPerson(tempPerson.getFirstName(),
						tempPerson.getLastName());
				int age = DateCalculator.ageCalculYears(tempMedicalRedord.getBirthdate(), LocalDate.now(), DATE_FORMAT);
				if (age < 18) {
					logger.debug("tempPerson : " + tempPerson);
					chidren.add(tempPerson);
				}
			}

		} else {
			logger.error(
					"Pour la requête GET il faut saisir un paramètre 'address'. Liste des paramètres saisis : "
							+ allParams.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Pour la requête GET il faut saisir un paramètre 'address'. Liste des paramètres saisis : "
							+ allParams.toString());
		}
		logger.debug("chidren : " + chidren);
		if (chidren.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun enfant à cette adresse ");
		}
		return chidren;
	}

	@GetMapping("/fire")
	@ResponseStatus(code = HttpStatus.FOUND)
	// http://localhost:8080/fire?address=<address>
	public List<MedicalDetailsPersonsAdress> getMedicalDetailsPersonsAdress(
			@RequestParam Map<String, String> allParams)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {

		logger.debug("allParams.size() : " + allParams.size());
		logger.debug("allParams : " + allParams.toString());

		List<MedicalDetailsPersonsAdress> medicalDetailsPersonsAdresses = new ArrayList<MedicalDetailsPersonsAdress>();

		String idAdress = allParams.get("address");
		if (allParams.size() == 1 && idAdress != null) {

			String idFireStation = fireStationService.getFireStationAdress(idAdress);

			List<Person> tempPersons = personService.getPersonAdress(idAdress);
			if (tempPersons.size() == 0) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Aucun personne à cette adresse");
			}

			for (Person tempPerson : tempPersons) {
				MedicalRecord tempMedicalRedord = medicalRecordService.getMedicalRecordPerson(tempPerson.getFirstName(),
						tempPerson.getLastName());
				medicalDetailsPersonsAdresses.add(
						new MedicalDetailsPersonsAdress(
								idFireStation,
								tempPerson.getFirstName(),
								tempPerson.getLastName(),
								tempPerson.getPhone(),
								DateCalculator.ageCalculYears(tempMedicalRedord.getBirthdate(),
										LocalDate.now(),
										DATE_FORMAT),
								tempMedicalRedord.getMedications(),
								tempMedicalRedord.getAllergies()));
			}

		} else {
			logger.error(
					"Pour la requête GET il faut saisir un paramètre 'address'. Liste des paramètres saisis : "
							+ allParams.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Pour la requête GET il faut saisir un paramètre 'address'. Liste des paramètres saisis : "
							+ allParams.toString());
		}
		logger.debug("medicalDetailsPersonsAdresses : " + medicalDetailsPersonsAdresses);
		if (medicalDetailsPersonsAdresses.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun enfant à cette adresse ");
		}
		return medicalDetailsPersonsAdresses;
	}

	@GetMapping("/communityEmail")
	@ResponseStatus(code = HttpStatus.FOUND)
	public List<String> getEmailsCity(
			@RequestParam Map<String, String> allParams)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {

		logger.debug("allParams.size() : " + allParams.size());
		logger.debug("allParams : " + allParams.toString());

		List<String> listEmails = new ArrayList<String>();

		String idCity = allParams.get("city");
		if (allParams.size() == 1 && idCity != null) {

			if (personService.getPersonCity(idCity).size() == 0) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Aucun personne à cette city");
			}
			for (Person tempPerson : personService.getPersonCity(idCity)) {
				if (tempPerson.getEmail() != null)
					listEmails.add(tempPerson.getEmail());
			}

		} else {
			logger.error(
					"Pour la requête GET il faut saisir un paramètre 'city'. Liste des paramètres saisis : "
							+ allParams.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Pour la requête GET il faut saisir un paramètre 'city'. Liste des paramètres saisis : "
							+ allParams.toString());
		}
		logger.debug("listEmails : " + listEmails);
		if (listEmails.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune adresse Email à cette city ");
		}
		listEmails = listEmails.stream()
				.distinct()
				.collect(Collectors.toList());
		return listEmails;
	}

}
