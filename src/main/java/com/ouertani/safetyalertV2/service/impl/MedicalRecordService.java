package com.ouertani.safetyalertV2.service.impl;

import org.springframework.stereotype.Service;

import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.service.IMedicalRecordService;

import lombok.Data;

@Data
@Service
public class MedicalRecordService implements IMedicalRecordService{

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
	public MedicalRecord getMedicalRecordPerson(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
