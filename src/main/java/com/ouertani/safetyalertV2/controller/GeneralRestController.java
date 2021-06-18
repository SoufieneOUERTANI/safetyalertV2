package com.ouertani.safetyalertV2.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.Mapping;
import com.ouertani.safetyalertV2.service.IMappingService;

@RestController
public class GeneralRestController {

	@Value("${JSON_FILE}")
	private String JSON_FILE;

	private IMappingService mappingService;

	@Autowired
	public GeneralRestController(IMappingService mappingService) {
		this.mappingService = mappingService;

	}

	@GetMapping("/")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public Mapping addPerson()
			throws JsonGenerationException, JsonMappingException, IOException {

		Mapping tempMapping = mappingService.readJsonFile(JSON_FILE);
		return (tempMapping);
	}

}
