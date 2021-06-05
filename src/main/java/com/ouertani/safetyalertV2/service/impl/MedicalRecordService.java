package com.ouertani.safetyalertV2.service.impl;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.constants.Constants;
import com.ouertani.safetyalertV2.model.Mapping;
import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.service.IMappingService;
import com.ouertani.safetyalertV2.service.IMedicalRecordService;

import lombok.Data;

@Data
@Service
public class MedicalRecordService implements IMedicalRecordService {

	private static final Logger logger = LogManager.getLogger("FireStationService");

	IMappingService mappingService;

	@Autowired
	public MedicalRecordService(IMappingService mappingService) {
		this.mappingService = mappingService;
	}

	@Override
	public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MedicalRecord putMedicalRecord(MedicalRecord medicalRecord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteMedicalRecord(String firstName, String lastName) {
		return false;
		// TODO Auto-generated method stub

	}

	@Override
	public MedicalRecord getMedicalRecordPerson(String firstName, String lastName)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("Paramètre " + "/ firstName : " + firstName + "/ lastName : " + lastName);
		Mapping tempMapping = mappingService.readJsonFile(Constants.JSON_FILE);
		logger.debug("tempMapping : " + tempMapping);
		for (MedicalRecord tempMedicalRecord : tempMapping.getMedicalrecords()) {
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
