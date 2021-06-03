package com.ouertani.safetyalertV2.service;

import com.ouertani.safetyalertV2.model.MedicalRecord;

public interface IMedicalRecordService {

	MedicalRecord addMedicalRecord(MedicalRecord medicalRecord);
	
	MedicalRecord putMedicalRecord(MedicalRecord medicalRecord);

	boolean deleteMedicalRecord(String firstName, String lastName);
	
	// ---
		
	MedicalRecord getMedicalRecordPerson(String firstName, String lastName);

	// ---

	//Optional<MedicalRecord> getMedicalRecord(long id);

	//Iterable<MedicalRecord> getMedicalRecords();

}
