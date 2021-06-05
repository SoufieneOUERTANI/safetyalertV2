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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	/*
	 * }catch(Exception e){ logger.error("Unable to process incoming vehicle",e); }
	 * 
	 * logger.info("Example log from {}", Example.class.getSimpleName());
	 */

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

	// add mapping for PUT /fireStations - update existing fireStation

	@PutMapping("/fireStation")
	public FireStation updateFireStation(@RequestBody FireStation theFireStation) {

		FireStation tempFireStation = fireStationService.putFireStation(theFireStation);
		if (tempFireStation == null) {
			throw new RuntimeException("Impossible to update the fireStation - " + theFireStation.getAddress() + " - "
					+ theFireStation.getStation());
		}
		return theFireStation;
	}

	// add mapping for DELETE /fireStations/{fireStationId} - delete fireStation

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

	/*
	 * http://localhost:8080/firestation?stationNumber=<station_number>
	 * 
	 * Cette url doit retourner une liste des personnes couvertes par la caserne de
	 * pompiers correspondante. Donc, si le numéro de station = 1, elle doit
	 * renvoyer les habitants couverts par la station numéro 1. La liste doit
	 * inclure les informations spécifiques suivantes : prénom, nom, adresse, numéro
	 * de téléphone. De plus, elle doit fournir un décompte du nombre d'adultes et
	 * du nombre d'enfants (tout individu âgé de 18 ans ou moins) dans la zone
	 * desservie.
	 ***
	 * 
	 * - Numéro de la station => "Get" dans "firestation" => List<Adress>
	 * getAdressFireStation(long IdFireStation) - Si la station trouvée => Déduire
	 * l'adresse 1 1 - List<Adress> getAdressFireStation(long IdFireStation)
	 * 
	 * - A patir de l'adresse => Get dans "Personnes" => List<Personne>
	 * getPersonAdress(Sting Adress) Si l'adresse correspond => Get date de
	 * naissance => MedicalRecord getMedicalRecordPerson(String firstName, String
	 * lastName) 2 - List<Personne> getPersonAdress(Sting Adress) - A partir de la
	 * date de naissance => Déterminer si adulte ou pas => boolean IsAdult(Date
	 * BirthDay) 4 - int getAge(Date BirthDay) => Period.between(birthDate,
	 * currentDate).getYears();
	 * 
	 * Renvoyer la liste DTO :
	 * 
	 * DTO
	 * 
	 * Int Nombre d'adultes Int Nomnre d'enfants
	 * 
	 * <personnes> (prénom, Nom, Adresse, numéro de téléphone)
	 ***
	 */

	@GetMapping("/fireStation")
	public GetFireStationClassReturn getFireStation(@RequestParam(defaultValue = "empty") String fireStationNumber)
			throws IncorrectParameterException {

		List<String> parameters = new ArrayList<String>();
		String Request = "Get";
		String URI = "/fireStation";
		parameters.add("fireStationNumber : " + fireStationNumber);

		logger.info("Request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters);

		GetFireStationClassReturn getFireStationClassReturn = null;

		try {

			try {
				Integer tempInt = Integer.parseInt(fireStationNumber);
			} catch (NumberFormatException e) {
				logger.warn("Request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters);
				throw new IncorrectParameterException("Merci de saisir un paramètre numérique");
			}

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
						// System.out.println(tempGetFireStationClass);
						getFireStationClassReturn.getPersonnes().add(tempGetFireStationClass);
						// System.out.println(getFireStationClassReturn);

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

		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("failed request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters,
					e);
			return null;
		}

		logger.info("Succeeded request " + Request + " sur l'URI " + URI + " : avec les paramètres : " + parameters);
		return getFireStationClassReturn;

	}
}
