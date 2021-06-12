package com.ouertani.safetyalertV2.service.impl;

import java.io.IOException;
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
import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.service.IMappingService;
import com.ouertani.safetyalertV2.service.IMedicalRecordService;

import lombok.Data;

@Data
@Service
public class MedicalRecordService implements IMedicalRecordService {

	@Value("${JSON_FILE}")
	private String JSON_FILE;

	private static final Logger logger = LogManager.getLogger("FireStationService");

	IMappingService mappingService;

	@Autowired
	public MedicalRecordService(IMappingService mappingService) {
		this.mappingService = mappingService;
	}

	@Override
	public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info(
				"Paramètre body : " + " medicalRecord : " + medicalRecord);

		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		logger.debug("tempMapping : " + tempMapping);

		List<ID> mappingPersonID = tempMapping.getPersons().stream()
				.map(c -> new ID(c.getFirstName(), c.getLastName())).collect(Collectors.toList());

		logger.debug("mappingPersonID : " + mappingPersonID);

		ID thePersonID = new ID(medicalRecord.getFirstName(), medicalRecord.getLastName());

		if (!mappingPersonID.contains(thePersonID)) {
			tempMapping.getMedicalRecords().add(medicalRecord);
		} else {
			tempMapping.getMedicalRecords().toString();
			logger.warn("L'objet existe déjà");
			logger.debug("medicalRecord : " + medicalRecord);
			logger.debug("mapping.getMedicalRecords().toString() : " + tempMapping.getMedicalRecords().toString());

			throw new ResponseStatusException(HttpStatus.CONFLICT, "Custom Error : L'objet existe déjà");
		}
		mappingService.writeJsonFile(JSON_FILE, tempMapping);
		return medicalRecord;
	}

	@Override
	public MedicalRecord putMedicalRecord(MedicalRecord medicalRecord)
			throws JsonGenerationException, JsonMappingException, IOException {

		logger.info(
				"Paramètre body : " + " medicalRecord : " + medicalRecord);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		int tempIndexOf = -1;
		for (MedicalRecord tempMedicalRecord : tempMapping.getMedicalRecords()) {
			if (tempMedicalRecord.getFirstName().equals(medicalRecord.getFirstName())
					&& tempMedicalRecord.getLastName().equals(medicalRecord.getLastName())) {
				tempIndexOf = tempMapping.getMedicalRecords().indexOf(tempMedicalRecord);
				break;
			}
		}

		if (tempIndexOf > -1) {
			tempMapping.getMedicalRecords().get(tempIndexOf).setAllergies(medicalRecord.getAllergies());
			tempMapping.getMedicalRecords().get(tempIndexOf).setBirthdate(medicalRecord.getBirthdate());
			tempMapping.getMedicalRecords().get(tempIndexOf).setMedications(medicalRecord.getMedications());

			mappingService.writeJsonFile(JSON_FILE, tempMapping);
			return tempMapping.getMedicalRecords().get(tempIndexOf);
		}
		return null;
	}

	@Override
	public MedicalRecord deleteMedicalRecord(String firstName, String lastName)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info(
				"Paramètre : " + " firstName : " + firstName + "/ lastName : " + lastName);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		MedicalRecord theMedicalRecord = null;
		for (MedicalRecord tempMedicalRecord : tempMapping.getMedicalRecords()) {
			if (tempMedicalRecord.getFirstName().equals(firstName)
					&& tempMedicalRecord.getLastName().equals(lastName)) {
				theMedicalRecord = tempMedicalRecord;
				break;
			}
		}
		if (theMedicalRecord != null) {
			tempMapping.getMedicalRecords().remove(theMedicalRecord);
			mappingService.writeJsonFile(JSON_FILE, tempMapping);
			return theMedicalRecord;
		}
		return null;
	}

	@Override
	public MedicalRecord getMedicalRecordPerson(String firstName, String lastName)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("Paramètre " + "/ firstName : " + firstName + "/ lastName : " + lastName);
		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		logger.debug("tempMapping : " + tempMapping);
		for (MedicalRecord tempMedicalRecord : tempMapping.getMedicalRecords()) {
			logger.debug("tempMedicalRecord : " + tempMedicalRecord);
			if (tempMedicalRecord.getFirstName().equals(firstName)
					&& tempMedicalRecord.getLastName().equals(lastName)) {
				logger.info("Returned MedicalRecord getMedicalRecordPerson : " + tempMedicalRecord);
				return tempMedicalRecord;
			}
		}
		logger.info("Returned MedicalRecord getMedicalRecordPerson : " + null);
		return null;
	}

}
