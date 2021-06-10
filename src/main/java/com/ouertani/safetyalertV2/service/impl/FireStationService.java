package com.ouertani.safetyalertV2.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.FireStation;
import com.ouertani.safetyalertV2.model.Mapping;
import com.ouertani.safetyalertV2.service.IFireStationService;
import com.ouertani.safetyalertV2.service.IMappingService;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Service
public class FireStationService implements IFireStationService {

	@Value("${JSON_FILE}")
	private String JSON_FILE;

	private static final Logger logger = LogManager.getLogger("FireStationService");

	IMappingService mappingService;

	@Autowired
	public FireStationService(IMappingService mappingService) {
		this.mappingService = mappingService;
	}

	@Override
	public FireStation addFireStation(FireStation firstStation)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info(
				"Paramètre body : " + " firstStation : " + firstStation);
		Mapping mapping = mappingService.readJsonFile(JSON_FILE);

		if (!mapping.getFirestations().contains(firstStation)) {
			mapping.getFirestations().add(firstStation);
		} else {
			mapping.getFirestations().toString();
			logger.warn("L'objet existe déjà");
			logger.debug("firstStation : " + firstStation);
			logger.debug("mapping.getFirestations().toString() : " + mapping.getFirestations().toString());

			throw new ResponseStatusException(HttpStatus.CONFLICT, "Custom Error : L'objet existe déjà");
		}
		mappingService.writeJsonFile(JSON_FILE, mapping);
		return firstStation;
	}

	@Override
	public FireStation putFireStation(FireStation firstStation)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info(
				"Paramètre body : " + " firstStation : " + firstStation);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		int tempIndexOf = -1;
		for (FireStation tempFireStation : tempMapping.getFirestations()) {
			if (tempFireStation.getAddress().equals(firstStation.getAddress())) {
				tempIndexOf = tempMapping.getFirestations().indexOf(tempFireStation);
				break;
			}
		}

		if (tempIndexOf > -1) {
			tempMapping.getFirestations().get(tempIndexOf).setStation(firstStation.getStation());
			mappingService.writeJsonFile(JSON_FILE, tempMapping);
			return tempMapping.getFirestations().get(tempIndexOf);
		}
		return null;
	}

	@Override
	public FireStation getFireStationAdress(String addressFireStation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FireStation> deleteFireStationAdress(String addressFireStation)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info(
				"Paramètre : " + " addressFireStation : " + addressFireStation);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		List<FireStation> tempFireStations = new ArrayList<FireStation>();
		for (FireStation tempFireStation : tempMapping.getFirestations()) {
			if (tempFireStation.getAddress().equals(addressFireStation)) {
				tempFireStations.add(tempFireStation);
			}
		}
		if (tempFireStations.size() != 0) {
			for (FireStation tempFireStation : tempFireStations) {
				tempMapping.getFirestations().remove(tempFireStation);
			}
			mappingService.writeJsonFile(JSON_FILE, tempMapping);
			return tempFireStations;
		}
		return null;
	}

	@Override
	public List<FireStation> deleteFireStationStation(String idFireStation)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info(
				"Paramètre : " + " idFireStation : " + idFireStation);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		List<FireStation> tempFireStations = new ArrayList<FireStation>();
		for (FireStation tempFireStation : tempMapping.getFirestations()) {
			if (tempFireStation.getStation().equals(idFireStation)) {
				tempFireStations.add(tempFireStation);
			}
		}
		if (tempFireStations.size() != 0) {
			for (FireStation tempFireStation : tempFireStations) {
				tempMapping.getFirestations().remove(tempFireStation);
			}
			mappingService.writeJsonFile(JSON_FILE, tempMapping);
			return tempFireStations;
		}
		return null;

	}

	public List<FireStation> deleteFireStation(String addressFireStation, String idFireStation)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info(
				"Paramètre : " + "/ addressFireStation : " + addressFireStation + "/ idFireStation : " + idFireStation);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		FireStation tempFireStation = new FireStation(addressFireStation, idFireStation);

		if (tempMapping.getFirestations().contains(tempFireStation)) {
			tempMapping.getFirestations().remove(tempFireStation);
		} else {
			logger.warn("L'objet n'existe pas");
			logger.debug("firstStation : " + tempFireStation);
			logger.debug("mapping.getFirestations().toString() : " + tempMapping.getFirestations().toString());

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Custom Error : L'objet n'existe pas");
		}
		mappingService.writeJsonFile(JSON_FILE, tempMapping);
		return Arrays.asList(tempFireStation);

	}

	@Override
	public List<String> getAdressFireStation(String idFireStation)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("Paramètre : " + "/ idFireStation : " + idFireStation);

		List<String> tempReturn = new ArrayList<String>();
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		logger.debug("tempMapping : " + tempMapping);
		for (FireStation tempFireStation : tempMapping.getFirestations()) {
			logger.debug("tempFireStation : " + tempFireStation);
			if (tempFireStation.getStation().equals(idFireStation)) {
				tempReturn.add(tempFireStation.getAddress());
				logger.debug("tempFireStation pris en compte : " + tempFireStation);
			}
		}
		logger.info("returned List<String> getAdressFireStation  : " + tempReturn);
		return tempReturn;
	}

}
