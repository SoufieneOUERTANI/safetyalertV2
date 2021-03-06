package com.ouertani.safetyalertV2.controller;

import java.io.IOException;
import java.text.ParseException;
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
import com.ouertani.safetyalertV2.dto.ListPersonsDetailsListStatioNumber;
import com.ouertani.safetyalertV2.dto.PersonsDetailsListStatioNumber;
import com.ouertani.safetyalertV2.model.FireStation;
import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.model.Person;
import com.ouertani.safetyalertV2.service.IFireStationService;
import com.ouertani.safetyalertV2.service.IMedicalRecordService;
import com.ouertani.safetyalertV2.service.IPersonService;
import com.ouertani.safetyalertV2.util.DateCalculator;

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
					"Pour la requ??te DELETE merci de saisir l'un des deux param??tres fireStationAdress ou stationNumber");

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
					"Pour la requ??te GET il faut saisir un param??tre 'fireStationNumber'. Liste des param??tres saisis : "
							+ allParams.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Pour la requ??te GET il faut saisir un param??tre 'fireStationNumber'. Liste des param??tres saisis : "
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
					"Request " + Request + " sur l'URI " + URI + " : avec les param??tres : " + parameters.toString()
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
					"failed request " + Request + " sur l'URI " + URI + " : avec les param??tres : " + parameters,
					exc);
			return null;
		}

		logger.info(
				"Succeeded request " + Request + " sur l'URI " + URI + " : avec les param??tres : " + parameters);
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
					"Pour la requ??te GET il faut saisir un param??tre 'phoneAlert'. Liste des param??tres saisis : "
							+ allParams.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Pour la requ??te GET il faut saisir un param??tre 'phoneAlert'. Liste des param??tres saisis : "
							+ allParams.toString());
		}
		if (listPhones.size() != 0) {
			listPhones = listPhones.stream()
					.distinct()
					.collect(Collectors.toList());
			return listPhones;
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Aucun num??ro de t??l??phen n'est asscoi?? ?? cette adresse : "
						+ allParams.toString());
	}

	@GetMapping("/flood/stations")
	@ResponseStatus(code = HttpStatus.FOUND)
	// http://localhost:8080/flood/stations?stations=<a list of station_numbers>
	public List<ListPersonsDetailsListStatioNumber> listListPersonsDetailsListStatioNumber(
			@RequestParam List<String> stations, @RequestParam Map<String, String> allParams)
			throws JsonGenerationException, JsonMappingException, IOException, ParseException {

		List<ListPersonsDetailsListStatioNumber> listListPersonsDetailsListStatioNumber = new ArrayList<ListPersonsDetailsListStatioNumber>();

		if (allParams == null || stations == null) {
			logger.error(
					"Pour la requ??te GET il faut saisir un param??tre 'stations'."
							+ allParams.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Pour la requ??te GET il faut saisir un param??tre 'stations'."
							+ allParams.toString());
		} else {
			if (allParams.size() > 1) {
				logger.error(
						"Pour la requ??te GET saisir le param??tre 'stations'. Les param??tres suivants ne sont pas permis : "
								+ allParams.keySet().stream().filter(c -> c.equals("stations") == false)
										.collect(Collectors.toList()));
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Pour la requ??te GET saisir le param??tre 'stations'. Les param??tres suivants ne sont pas permis : "
								+ allParams.keySet().stream().filter(c -> c.equals("stations") == false)
										.collect(Collectors.toList()));
			}
		}
		logger.debug("stations : " + stations);
		List<String> adresses = new ArrayList<String>();
		for (String tempStation : stations) {
			adresses.addAll(fireStationService.getAdressFireStation(tempStation));
		}
		if (adresses != null)
			if (adresses.size() != 0) {
				adresses = adresses.stream().distinct().filter(c -> c != null).collect(Collectors.toList());
			} else {
				logger.info("Aucune adresse ne correspond ?? cette station : ");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aucune adresse ne correspond ?? station : ");
			}
		else {
			logger.info("Aucune adresse ne correspond ?? cette station : ");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aucune adresse ne correspond ?? station : ");
		}
		ListPersonsDetailsListStatioNumber listPersonsDetailsListStatioNumber;

		for (String tempAdress : adresses) {
			listPersonsDetailsListStatioNumber = new ListPersonsDetailsListStatioNumber();
			listPersonsDetailsListStatioNumber
					.setListPersonsDetailsListStatioNumber(new ArrayList<PersonsDetailsListStatioNumber>());
			List<Person> tempPersons = personService.getPersonAdress(tempAdress);
			if (tempPersons != null)
				if (tempPersons.size() != 0) {

					for (Person tempPerson : tempPersons) {
						PersonsDetailsListStatioNumber personsDetailsListStatioNumber = new PersonsDetailsListStatioNumber(
								tempPerson.getFirstName(), tempPerson.getLastName(), tempPerson.getPhone(), 0, null,
								null);
						MedicalRecord tempMedicalRecord = medicalRecordService
								.getMedicalRecordPerson(tempPerson.getFirstName(), tempPerson.getLastName());
						if (tempMedicalRecord != null) {
							if (tempMedicalRecord.getBirthdate() != null)
								personsDetailsListStatioNumber.setAge(DateCalculator
										.ageCalculYears(tempMedicalRecord.getBirthdate(), LocalDate.now(),
												DATE_FORMAT));
							personsDetailsListStatioNumber.setMedications(tempMedicalRecord.getMedications());
							personsDetailsListStatioNumber.setAllergies(tempMedicalRecord.getAllergies());
						}
						listPersonsDetailsListStatioNumber.getListPersonsDetailsListStatioNumber()
								.add(personsDetailsListStatioNumber);
					}
					if (listPersonsDetailsListStatioNumber != null)
						if (listPersonsDetailsListStatioNumber.getListPersonsDetailsListStatioNumber() != null)
							if (listPersonsDetailsListStatioNumber.getListPersonsDetailsListStatioNumber().size() != 0)
								listPersonsDetailsListStatioNumber.setAdress(tempAdress);

				}
			if (listPersonsDetailsListStatioNumber != null)
				listListPersonsDetailsListStatioNumber.add(listPersonsDetailsListStatioNumber);
		}
		if (listListPersonsDetailsListStatioNumber != null)
			if (listListPersonsDetailsListStatioNumber.size() != 0) {
				logger.debug(
						"SOUE77 listListPersonsDetailsListStatioNumber : " + listListPersonsDetailsListStatioNumber);
				return listListPersonsDetailsListStatioNumber;
			}
		return null;
	}

}
