package com.ouertani.safetyalertV2.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.FireStation;

@Component
public interface IFireStationService {

	FireStation addFireStation(FireStation firstStation)
			throws JsonGenerationException, JsonMappingException, IOException;

	FireStation putFireStation(FireStation firstStation)
			throws JsonGenerationException, JsonMappingException, IOException;

	List<FireStation> deleteFireStationAdress(String address)
			throws JsonGenerationException, JsonMappingException, IOException;

	List<FireStation> deleteFireStationStation(String station)
			throws JsonGenerationException, JsonMappingException, IOException;

	List<FireStation> deleteFireStation(String address, String station)
			throws JsonGenerationException, JsonMappingException, IOException;

	// ---

	List<String> getAdressFireStation(String idFireStation)
			throws JsonGenerationException, JsonMappingException, IOException;

	String getFireStationAdress(String Adress) throws JsonGenerationException, JsonMappingException, IOException;

	// ---

	// Optional<FirstStation> getFirstStation(long id);

	// Iterable<FirstStation> getFirstStations();

}
