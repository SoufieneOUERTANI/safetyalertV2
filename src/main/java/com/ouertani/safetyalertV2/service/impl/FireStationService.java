package com.ouertani.safetyalertV2.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.FireStation;
import com.ouertani.safetyalertV2.service.IFireStationService;
import com.ouertani.safetyalertV2.service.IMappingService;

import lombok.Data;

@Data
@Service
public class FireStationService implements IFireStationService{
	
	IMappingService mappingService;
	
	@Autowired
	public FireStationService(IMappingService mappingService) {
		this.mappingService = mappingService;
	}

	@Override
	public FireStation addFireStation(FireStation firstStation) throws JsonGenerationException, JsonMappingException, IOException {
		/*
		Mapping mapping = mappingService.readJsonFile();
		mapping.getFirestations().add(firstStation);
		mappingService.writeJsonFile(mapping);
		 */
		return firstStation;
	}

	@Override
	public FireStation putFireStation(FireStation firstStation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FireStation getFireStationAdress(String Adress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteFireStationAdress(String address) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFireStationStation(String station) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getAdressFireStation(String idFireStation) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
