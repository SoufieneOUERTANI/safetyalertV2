package com.ouertani.safetyalertV2.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.MedicalRecord;

public interface IMedicalRecordService {

	MedicalRecord addMedicalRecord(MedicalRecord medicalRecord)
			throws JsonGenerationException, JsonMappingException, IOException;

	MedicalRecord putMedicalRecord(MedicalRecord medicalRecord)
			throws JsonGenerationException, JsonMappingException, IOException;

	MedicalRecord deleteMedicalRecord(String firstName, String lastName)
			throws JsonGenerationException, JsonMappingException, IOException;

	// ---

	MedicalRecord getMedicalRecordPerson(String firstName, String lastName)
			throws JsonGenerationException, JsonMappingException, IOException;

	// ---

	// Optional<MedicalRecord> getMedicalRecord(long id);

	// Iterable<MedicalRecord> getMedicalRecords();

}
