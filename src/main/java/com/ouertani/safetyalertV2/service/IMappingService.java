/**
 * 
 */
package com.ouertani.safetyalertV2.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ouertani.safetyalertV2.model.Mapping;

/**
 * @author SOUE
 *
 */
@Service
public interface IMappingService {

	public Mapping readJsonFile() throws JsonGenerationException, JsonMappingException, IOException;
	
	public void writeJsonFile(Mapping mapping) throws JsonGenerationException, JsonMappingException, IOException;

}
