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
import com.ouertani.safetyalertV2.constants.Constants;
import com.ouertani.safetyalertV2.dto.FileJsonMapping;
import com.ouertani.safetyalertV2.model.Mapping;
import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.service.IMappingService;
import com.ouertani.safetyalertV2.service.IMedicalRecordService;
import com.ouertani.safetyalertV2.service.impl.MappingService;


@RestController
public class MedicalRecordRestController {

	private IMedicalRecordService medicalRecordService;
	
	@Autowired
	public MedicalRecordRestController(IMedicalRecordService theMedicalRecordService) {
		medicalRecordService = theMedicalRecordService;
	}
	
	/*
	@GetMapping("/medicalRecords")
	public List<MedicalRecord> getMedicalRecords() {
		return (List<MedicalRecord>) medicalRecordService.getMedicalRecords();
	}
	*/

	/*
	@GetMapping("/medicalRecords/{medicalRecordId}")
	public Optional<MedicalRecord> getMedicalRecord(@PathVariable long medicalRecordId) {
		
		Optional<MedicalRecord> theMedicalRecord = medicalRecordService.getMedicalRecord(medicalRecordId);
		
		if (theMedicalRecord == null) {
			throw new RuntimeException("MedicalRecord id not found - " + medicalRecordId);
		}
		
		return theMedicalRecord;
	}
	*/
	
	@GetMapping("/")
	public Mapping global() throws JsonGenerationException, JsonMappingException, IOException {
		
		IMappingService mappingService = new MappingService();
		return FileJsonMapping.mapping = mappingService.readJsonFile(Constants.JSON_FILE);

	}
	
	
	@PostMapping("/medicalRecord")
	public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord theMedicalRecord) {
		
		MedicalRecord tempMedicalRecord = medicalRecordService.addMedicalRecord(theMedicalRecord);
		if (tempMedicalRecord == null) {
			throw new RuntimeException("Impossible to add the medicalRecord - " + theMedicalRecord.getFirstName() + " - "+ theMedicalRecord.getLastName());
		}
		return(medicalRecordService.addMedicalRecord(theMedicalRecord));
	}
	
	// add mapping for PUT /medicalRecords - update existing medicalRecord
	
	@PutMapping("/medicalRecord")
	public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord theMedicalRecord) {
		
		MedicalRecord tempMedicalRecord = medicalRecordService.putMedicalRecord(theMedicalRecord);
		if (tempMedicalRecord == null) {
			throw new RuntimeException("Impossible to update the medicalRecord - " + theMedicalRecord.getFirstName() + " - "+ theMedicalRecord.getLastName());
		}
		return theMedicalRecord;
	}
	
	// add mapping for DELETE /medicalRecords/{medicalRecordId} - delete medicalRecord
	
	@DeleteMapping("/medicalRecord")
	public String deleteMedicalRecord(@RequestParam(defaultValue = "empty") String firstName, @RequestParam(defaultValue = "empty") String lastName) {
		if (firstName == "empty" || lastName == "empty") 
			return "Please give a \"firstName\" and a \"lastName\""; 
		
		boolean tempMedicalRecord = medicalRecordService.deleteMedicalRecord(firstName, lastName);
		if (tempMedicalRecord == false) {
			throw new RuntimeException("MedicalRecord not found - " + firstName + " - "+ lastName);
		}
		return "Deleted medicalRecord id - " + firstName + " - "+ lastName;
	}	

}
