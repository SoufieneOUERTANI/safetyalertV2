package com.ouertani.safetyalertV2.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouertani.safetyalertV2.model.Mapping;
import com.ouertani.safetyalertV2.service.IMappingService;

@Service
public class MappingService implements IMappingService {

	private static final Logger logger = LogManager.getLogger("MappingService");

	public Mapping readJsonFile(String json_file) throws JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return (objectMapper.readValue(new File(json_file), Mapping.class));

	}

	public void writeJsonFile(String json_file, Mapping mapping)
			throws JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(new File(json_file), mapping);

	}
}
