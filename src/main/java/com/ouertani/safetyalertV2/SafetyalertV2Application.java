package com.ouertani.safetyalertV2;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootApplication
//@EnableSwagger2
public class SafetyalertV2Application {

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		SpringApplication.run(SafetyalertV2Application.class, args);
	}

}
