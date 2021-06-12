package com.ouertani.safetyalertV2.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.service.IMedicalRecordService;

@RestController
public class MedicalRecordRestController {

	@Value("${JSON_FILE}")
	private String JSON_FILE;

	private IMedicalRecordService medicalRecordService;

	@Autowired
	public MedicalRecordRestController(IMedicalRecordService theMedicalRecordService) {
		medicalRecordService = theMedicalRecordService;
	}

	@PostMapping("/medicalRecord")
	@ResponseStatus(code = HttpStatus.CREATED)
	public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord theMedicalRecord)
			throws JsonGenerationException, JsonMappingException, IOException {

		MedicalRecord tempMedicalRecord = medicalRecordService.addMedicalRecord(theMedicalRecord);
		if (tempMedicalRecord == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Impossible to add the medicalRecord - " + theMedicalRecord.getFirstName() + " - "
							+ theMedicalRecord.getLastName());
		}
		return (tempMedicalRecord);
	}

	@PutMapping("/medicalRecord")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord theMedicalRecord)
			throws JsonGenerationException, JsonMappingException, IOException {

		MedicalRecord tempMedicalRecord = medicalRecordService.putMedicalRecord(theMedicalRecord);
		if (tempMedicalRecord == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Custom Error : Impossible to update the medicalRecord - "
							+ theMedicalRecord.getFirstName() + " - " + theMedicalRecord.getLastName());
		}
		return theMedicalRecord;
	}

	@DeleteMapping("/medicalRecord")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public String deleteMedicalRecord(@RequestParam(defaultValue = "empty") String firstName,
			@RequestParam(defaultValue = "empty") String lastName)
			throws JsonGenerationException, JsonMappingException, IOException {
		if (firstName.equals("empty") || lastName.equals("empty"))
			return "Please give a \"firstName\" and a \"lastName\"";

		MedicalRecord tempMedicalRecord = medicalRecordService.deleteMedicalRecord(firstName, lastName);
		if (tempMedicalRecord == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"MedicalRecord not found - " + firstName + " - " + lastName);
		}
		return "Deleted medicalRecord id - " + firstName + " - " + lastName;
	}

}
