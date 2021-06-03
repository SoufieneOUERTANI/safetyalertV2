package com.ouertani.safetyalertV2.service.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouertani.safetyalertV2.model.Mapping;
import com.ouertani.safetyalertV2.service.IMappingService;

@Service
public class MappingService implements IMappingService {
		
	public Mapping readJsonFile() throws JsonGenerationException, JsonMappingException, IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		return(objectMapper.readValue(new File("src/main/resources/json/data.json"),Mapping.class));
		
	}
	
	public void writeJsonFile(Mapping mapping) throws JsonGenerationException, JsonMappingException, IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(new File("src/main/resources/json/data.json"),mapping);
		
	}
}
