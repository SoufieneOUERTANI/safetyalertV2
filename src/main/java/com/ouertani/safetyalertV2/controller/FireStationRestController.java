package com.ouertani.safetyalertV2.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.constants.Constants;
import com.ouertani.safetyalertV2.dto.GetFireStationClass;
import com.ouertani.safetyalertV2.dto.GetFireStationClassReturn;
import com.ouertani.safetyalertV2.model.FireStation;
import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.model.Person;
import com.ouertani.safetyalertV2.service.IFireStationService;
import com.ouertani.safetyalertV2.service.IMedicalRecordService;
import com.ouertani.safetyalertV2.service.IPersonService;
import com.ouertani.safetyalertV2.util.IncorrectParameterException;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@RestController
public class FireStationRestController {

	private static final Logger logger = LogManager.getLogger("FireStationRestController");

	private IFireStationService fireStationService;
	private IPersonService personService;
	private IMedicalRecordService medicalRecordService;

	@Autowired
	public FireStationRestController(IFireStationService theFireStationService, IPersonService thePersonService,
			IMedicalRecordService theMedicalRecordService) {
		fireStationService = theFireStationService;
		personService = thePersonService;
		medicalRecordService = theMedicalRecordService;
	}

	@PostMapping("/fireStation")
	public FireStation addFireStation(@RequestBody FireStation theFireStation)
			throws JsonGenerationException, JsonMappingException, IOException {

		FireStation tempFireStation = fireStationService.addFireStation(theFireStation);
		if (tempFireStation == null) {
			throw new RuntimeException("Impossible to add the fireStation - " + theFireStation.getAddress() + " - "
					+ theFireStation.getStation());
		}
		return (fireStationService.addFireStation(theFireStation));
	}

	@PutMapping("/fireStation")
	public FireStation updateFireStation(@RequestBody FireStation theFireStation) {

		FireStation tempFireStation = fireStationService.putFireStation(theFireStation);
		if (tempFireStation == null) {
			throw new RuntimeException("Impossible to update the fireStation - " + theFireStation.getAddress() + " - "
					+ theFireStation.getStation());
		}
		return theFireStation;
	}

	@DeleteMapping("/fireStation")
	public String deleteFireStation(@RequestParam(defaultValue = "empty") String address,
			@RequestParam(defaultValue = "empty") String station) {
		if (address == "empty" && station == "empty")
			return "Please give either an \"address\" and a \"station\"";
		if (address != "empty" && station != "empty")
			return "Please either the \"address\" or the \"station\" should be in parameter";

		boolean tempFireStation = false;
		if (address != "empty")
			tempFireStation = fireStationService.deleteFireStationAdress(address);
		else
			tempFireStation = fireStationService.deleteFireStationStation(station);

		if (tempFireStation == false) {
			throw new RuntimeException("FireStation not found - " + ((address == "empty") ? "" : address) + " - "
					+ ((station == "empty") ? "" : station));
		}
		return "Deleted fireStation id - " + address + " - " + station;
	}

	// ---------------------

	@GetMapping("/fireStation")
	public GetFireStationClassReturn getFireStation(@RequestParam(defaultValue = "empty") String fireStationNumber)
			throws IncorrectParameterException {

		List<String> parameters = new ArrayList<String>();
		String Request = "Get";
		String URI = "/fireStation";
		parameters.add("fireStationNumber : " + fireStationNumber);

		logger.info("Request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters);

		if (fireStationNumber == "empty") {
			logger.warn(
					"Request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters.toString());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Custom Error : empty request parameter");

		}

		try {
			Integer tempInt = Integer.parseInt(fireStationNumber);
		} catch (NumberFormatException exc) {
			logger.warn("Request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters.toString()
					+ " Exception : " + exc.toString());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Custom Error : Non numeric request parameter",
					exc);
		}

		GetFireStationClassReturn getFireStationClassReturn = null;

		try {

			getFireStationClassReturn = new GetFireStationClassReturn();
			getFireStationClassReturn.setAdultNum(0);
			getFireStationClassReturn.setChildNum(0);
			getFireStationClassReturn.setPersonnes(new ArrayList<GetFireStationClass>());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

			List<String> tempAdresses;
			List<Person> tempPersons = null;
			MedicalRecord tempMedicalRecord;

			tempAdresses = fireStationService.getAdressFireStation(fireStationNumber);
			if (tempAdresses != null) {
				for (String adress : tempAdresses) {
					tempPersons = personService.getPersonAdress(adress);
					for (Person tempPerson : tempPersons) {
						tempMedicalRecord = medicalRecordService.getMedicalRecordPerson(tempPerson.getFirstName(),
								tempPerson.getLastName());
						GetFireStationClass tempGetFireStationClass = new GetFireStationClass(tempPerson.getFirstName(),
								tempPerson.getLastName(), tempPerson.getAddress(), tempPerson.getPhone());
						getFireStationClassReturn.getPersonnes().add(tempGetFireStationClass);

						if (Period.between(LocalDate.parse(tempMedicalRecord.getBirthdate(), formatter),
								new Date(System.currentTimeMillis()).toInstant()
										.atZone(ZoneId.systemDefault())
										.toLocalDate())
								.getYears() > 17)
							getFireStationClassReturn.setAdultNum(getFireStationClassReturn.getAdultNum() + 1);
						else
							getFireStationClassReturn.setChildNum(getFireStationClassReturn.getChildNum() + 1);
					}
				}
			}

		} catch (Exception exc) {
			// e.printStackTrace();
			logger.error("failed request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters,
					exc);
			return null;
		}

		logger.info("Succeeded request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters);
		return getFireStationClassReturn;
	}
}
