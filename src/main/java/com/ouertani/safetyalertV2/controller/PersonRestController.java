package com.ouertani.safetyalertV2.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.dto.FileJsonMapping;
import com.ouertani.safetyalertV2.model.Mapping;
import com.ouertani.safetyalertV2.model.Person;
import com.ouertani.safetyalertV2.service.IMappingService;
import com.ouertani.safetyalertV2.service.IPersonService;
import com.ouertani.safetyalertV2.service.impl.MappingService;


@RestController
public class PersonRestController {

	private IPersonService personService;
	
	@Autowired
	public PersonRestController(IPersonService thePersonService) {
		personService = thePersonService;
	}
	
	/*
	@GetMapping("/persons")
	public List<Person> getPersons() {
		return (List<Person>) personService.getPersons();
	}
	*/

	/*
	@GetMapping("/persons/{personId}")
	public Optional<Person> getPerson(@PathVariable long personId) {
		
		Optional<Person> thePerson = personService.getPerson(personId);
		
		if (thePerson == null) {
			throw new RuntimeException("Person id not found - " + personId);
		}
		
		return thePerson;
	}
	*/
	
	/*
	@GetMapping("/")
	public Mapping global() throws JsonGenerationException, JsonMappingException, IOException {
		
		IMappingService mappingService = new MappingService();
		return FileJsonMapping.mapping = mappingService.readJsonFile();

	}
	*/
	
	@PostMapping("/person")
	public Person addPerson(@RequestBody Person thePerson) {
		
		Person tempPerson = personService.addPerson(thePerson);
		if (tempPerson == null) {
			throw new RuntimeException("Impossible to add the person - " + thePerson.getFirstName() + " - "+ thePerson.getLastName());
		}
		return(personService.addPerson(thePerson));
	}
	
	@PutMapping("/person")
	public Person updatePerson(@RequestBody Person thePerson) {
		
		Person tempPerson = personService.putPerson(thePerson);
		if (tempPerson == null) {
			throw new RuntimeException("Impossible to update the person - " + thePerson.getFirstName() + " - "+ thePerson.getLastName());
		}
		return thePerson;
	}
	
	@DeleteMapping("/person")
	public String deletePerson(@RequestParam(defaultValue = "empty") String firstName, @RequestParam(defaultValue = "empty") String lastName) {
		if (firstName == "empty" || lastName == "empty") 
			return "Please give a \"firstName\" and a \"lastName\""; 
		
		boolean tempPerson = personService.deletePerson(firstName, lastName);
		if (tempPerson == false) {
			throw new RuntimeException("Person not found - " + firstName + " - "+ lastName);
		}
		return "Deleted person id - " + firstName + " - "+ lastName;
	}
	
/*
	----------------------------------

	http://localhost:8080/childAlert?address=<address>
	Cette url doit retourner une liste d'enfants (tout individu âgé de 18 ans ou moins) habitant à cette adresse.
	La liste doit comprendre le prénom et le nom de famille de chaque enfant, son âge et une liste des autres
	membres du foyer. S'il n'y a pas d'enfant, cette url peut renvoyer une chaîne vide.
	***
	2 - List<Personne> getPersonAdress(Sting Adress)
	3 - MedicalRecord getMedicalRecordPerson(String firstName, String lastName)
	4 - int getAge(Date BirthDay) => Period.between(birthDate, currentDate).getYears();

	DTO


	List <Enfant>  
	List <Adulte>
*/
	/*
	@GetMapping("/childAlert")
	public String childAlert(@RequestParam(defaultValue = "empty") String address) {
		if (firstName == "empty" || lastName == "empty") 
			return "Please give a \"firstName\" and a \"lastName\""; 
		
		boolean tempPerson = personService.deletePerson(firstName, lastName);
		if (tempPerson == false) {
			throw new RuntimeException("Person not found - " + firstName + " - "+ lastName);
		}
		return "Deleted person id - " + firstName + " - "+ lastName;
	}
	*/
	
}
