package com.ouertani.safetyalertV2.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import com.ouertani.safetyalertV2.dto.GetFireStationClass;
import com.ouertani.safetyalertV2.dto.GetFireStationClassReturn;
import com.ouertani.safetyalertV2.model.FireStation;
import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.model.Person;
import com.ouertani.safetyalertV2.service.IFireStationService;
import com.ouertani.safetyalertV2.service.IMedicalRecordService;
import com.ouertani.safetyalertV2.service.IPersonService;

@RestController
public class FireStationRestController {

	private static final Logger logger = LogManager.getLogger("FireStationRestController");

	@Value("${spring.jackson.date-format}")
	private String DATE_FORMAT;

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

	@PostMapping("/firestation")
	@ResponseStatus(code = HttpStatus.CREATED)
	public FireStation addFireStation(@RequestBody FireStation theFireStation)
			throws JsonGenerationException, JsonMappingException, IOException {
		if (theFireStation == null) {
			throw new RuntimeException("Impossible to add the null fireStation ");
		}
		FireStation tempFireStation = fireStationService.addFireStation(theFireStation);
		return (tempFireStation);
	}

	@PutMapping("/firestation")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public FireStation updateFireStation(@RequestBody FireStation theFireStation)
			throws JsonGenerationException, JsonMappingException, IOException {

		FireStation tempFireStation = fireStationService.putFireStation(theFireStation);
		if (tempFireStation == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Impossible to update the fireStation - " + theFireStation.getAddress() + " - "
							+ theFireStation.getStation());
		}
		return theFireStation;
	}

	@DeleteMapping("/firestation")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public String deleteFireStation(@RequestParam(defaultValue = "empty") String fireStationAdress,
			@RequestParam(defaultValue = "empty") String stationNumber)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info(fireStationAdress + "/" + stationNumber);

		if (fireStationAdress.equals("empty") && stationNumber.equals("empty"))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Pour la requête DELETE merci de saisir l'un des deux paramètres fireStationAdress ou stationNumber");

		List<FireStation> tempFireStation = new ArrayList<FireStation>();
		if (!fireStationAdress.equals("empty") && stationNumber.equals("empty"))
			tempFireStation = fireStationService.deleteFireStationAdress(fireStationAdress);
		if (!stationNumber.equals("empty") && fireStationAdress.equals("empty"))
			tempFireStation = fireStationService.deleteFireStationStation(stationNumber);
		if (!stationNumber.equals("empty") && !fireStationAdress.equals("empty"))
			tempFireStation = fireStationService.deleteFireStation(fireStationAdress, stationNumber);

		if (tempFireStation == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"FireStation not found - " + ((fireStationAdress.equals("empty")) ? "" : fireStationAdress) + " - "
							+ ((stationNumber.equals("empty")) ? "" : stationNumber));
		}
		if (tempFireStation.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"FireStation not found - " + ((fireStationAdress.equals("empty")) ? "" : fireStationAdress) + " - "
							+ ((stationNumber.equals("empty")) ? "" : stationNumber));
		}
		return ("Deleted fireStations - " + tempFireStation.toString());
	}

	// ---------------------
	/*
	 * http://localhost:8080/firestation?stationNumber=<station_number> Cette url
	 * doit retourner une liste des personnes couvertes par la caserne de pompiers
	 * correspondante. Donc, si le numéro de station = 1, elle doit renvoyer les
	 * habitants couverts par la station numéro 1. La liste doit inclure les
	 * informations spécifiques suivantes : prénom, nom, adresse, numéro de
	 * téléphone. De plus, elle doit fournir un décompte du nombre d'adultes et du
	 * nombre d'enfants (tout individu âgé de 18 ans ou moins) dans la zone
	 * desservie.
	 */
	/*
	 * http://localhost:8080/firestation Cet endpoint permettra d’effectuer les
	 * actions suivantes via Post/Put/Delete avec http : ● ajout d'un mapping
	 * caserne/adresse ; ● mettre à jour le numéro de la caserne de pompiers d'une
	 * adresse ; ● supprimer le mapping d'une caserne ou d'une adresse.
	 */

//	@GetMapping("/firestation")
//	public GetFireStationClassReturn getFireStation(
//			@RequestParam(
//					// defaultValue = "empty",
//					required = false) String fireStationNumber)

	@GetMapping("/firestation")
	@ResponseStatus(code = HttpStatus.FOUND)
	public GetFireStationClassReturn getFireStation(
			@RequestParam Map<String, String> allParams) {

		logger.debug("allParams.size() : " + allParams.size());
		logger.debug("allParams : " + allParams.toString());

		String stationNumber = allParams.get("stationNumber");
		if (allParams.size() == 1 && stationNumber != null) {
			return getFireStationFireStationNumber(stationNumber);
		} else {
			logger.error(
					"Pour la requête GET il faut saisir un paramètre 'fireStationNumber'. Liste des paramètres saisis : "
							+ allParams.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Pour la requête GET il faut saisir un paramètre 'fireStationNumber'. Liste des paramètres saisis : "
							+ allParams.toString());
		}

	}

	/**
	 * @param fireStationNumber
	 * @return
	 * @throws ResponseStatusException
	 */
	private GetFireStationClassReturn getFireStationFireStationNumber(String fireStationNumber)
			throws ResponseStatusException {
		List<String> parameters = new ArrayList<String>();
		String Request = "Get";
		String URI = "/firestation";
		parameters.add("fireStationNumber : " + fireStationNumber);

		try {
			Integer tempInt = Integer.parseInt(fireStationNumber);
		} catch (NumberFormatException exc) {
			logger.warn(
					"Request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters.toString()
							+ " Exception : " + exc.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Custom Error : Non numeric request parameter",
					exc);
		}

		GetFireStationClassReturn getFireStationClassReturn = null;

		try {
			getFireStationClassReturn = new GetFireStationClassReturn();
			getFireStationClassReturn.setAdultNum(0);
			getFireStationClassReturn.setChildNum(0);
			getFireStationClassReturn.setPersonnes(new ArrayList<GetFireStationClass>());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

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
						GetFireStationClass tempGetFireStationClass = new GetFireStationClass(
								tempPerson.getFirstName(),
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
			logger.error(
					"failed request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters,
					exc);
			return null;
		}

		logger.info(
				"Succeeded request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters);
		return getFireStationClassReturn;
	}

	@GetMapping("/phoneAlert")
	@ResponseStatus(code = HttpStatus.FOUND)
	public List<String> getPersonsPhoneFireStation(
			@RequestParam Map<String, String> allParams)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.debug("allParams.size() : " + allParams.size());
		logger.debug("allParams : " + allParams.toString());

		List<String> listPhones = new ArrayList<String>();

		String idFirestation = allParams.get("firestation");
		if (allParams.size() == 1 && idFirestation != null) {
			List<String> listAdressesFireStation = fireStationService.getAdressFireStation(idFirestation);
			for (String AdressesFireStation : listAdressesFireStation) {
				for (Person tempPerson : personService.getPersonAdress(AdressesFireStation)) {
					if (tempPerson.getPhone() != null)
						listPhones.add(tempPerson.getPhone());
				}
			}
		} else {
			logger.error(
					"Pour la requête GET il faut saisir un paramètre 'phoneAlert'. Liste des paramètres saisis : "
							+ allParams.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Pour la requête GET il faut saisir un paramètre 'phoneAlert'. Liste des paramètres saisis : "
							+ allParams.toString());
		}
		if (listPhones.size() != 0) {
			listPhones = listPhones.stream()
					.distinct()
					.collect(Collectors.toList());
			return listPhones;
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Aucun numéro de téléphen n'est asscoié à cette adresse : "
						+ allParams.toString());
	}

}
