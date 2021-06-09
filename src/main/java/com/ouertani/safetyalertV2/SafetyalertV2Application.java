package com.ouertani.safetyalertV2;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.service.IMappingService;
import com.ouertani.safetyalertV2.service.impl.MappingService;

@SpringBootApplication
public class SafetyalertV2Application {

	static IMappingService mappingService = new MappingService();

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		SpringApplication.run(SafetyalertV2Application.class, args);
	}

}
