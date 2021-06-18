package com.ouertani.safetyalertV2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouertani.safetyalertV2.model.ID;
import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.service.IMappingService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordRestControllerTest {

	private static final Logger logger = LogManager.getLogger("MedicalRecordRestController");

	@Value("${JSON_FILE}")
	private String JSON_FILE;

	@Value("${JSON_FILE_SAVE}")
	private String JSON_FILE_SAVE;

	@Autowired
	IMappingService mappingService;

	@Autowired
	private MockMvc medicalRecordRestControllerMockMvc;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {

		Path source = Paths.get(JSON_FILE_SAVE);
		Path target = Paths.get(JSON_FILE);
		try {
			System.out.println(Files.deleteIfExists(target));
			System.out.println(Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@AfterEach
	void tearDown() throws Exception {
	}

//	@Test
//	final void testMedicalRecordRestController() {
//		fail("Not yet implemented"); // TODO
//	}

	@Test
	final void testPostMedicalRecord_NotAlreadyExist_isCreated() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int medicalRecordsSize_Before = mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ medicalRecordsSize_Before);

		MedicalRecord tempMedicalRecord = new MedicalRecord("firstName", "lastName", "15/03/2018",
				Arrays.asList("medication1", "mdeciation2"),
				Arrays.asList("allergy1", "allergy2", "allergy3"));

		assertEquals(mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord), false);

		String tempMedicalRecordJson = mapper.writeValueAsString(tempMedicalRecord);

		mvcResult = medicalRecordRestControllerMockMvc.perform(MockMvcRequestBuilders
				.post("/medicalRecord")
				.content(tempMedicalRecordJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size());

		assertEquals(mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord), true);
		assertEquals(1, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size() - medicalRecordsSize_Before);

	}

	@Test
	final void testPostMedicalRecord_AlreadyExist_isConflict() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int medicalRecordsSize_Before = mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ medicalRecordsSize_Before);

		MedicalRecord tempMedicalRecord = new MedicalRecord("Allison", "Boyd", "15/03/2018",
				Arrays.asList("medication1", "mdeciation2"),
				Arrays.asList("allergy1", "allergy2", "allergy3"));

		List<ID> mappingPersonID = mappingService.readJsonFile(JSON_FILE).getPersons().stream()
				.map(c -> new ID(c.getFirstName(), c.getLastName())).collect(Collectors.toList());

		logger.debug("mappingPersonID : " + mappingPersonID);

		ID thePersonID = new ID(tempMedicalRecord.getFirstName(), tempMedicalRecord.getLastName());

		logger.debug("thePersonID : " + thePersonID);

		assertEquals(true, mappingPersonID.contains(thePersonID));

		String tempMedicalRecordJson = mapper.writeValueAsString(tempMedicalRecord);

		mvcResult = medicalRecordRestControllerMockMvc.perform(MockMvcRequestBuilders
				.post("/medicalRecord")
				.content(tempMedicalRecordJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andReturn();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size());

		// assertEquals(false,
		// mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord));
		assertEquals(0, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size() - medicalRecordsSize_Before);

	}

	@Test
	final void testPostMedicalRecord_WithoutBody_isBadRequest() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int medicalRecordsSize_Before = mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ medicalRecordsSize_Before);

		mvcResult = medicalRecordRestControllerMockMvc.perform(MockMvcRequestBuilders
				.post("/medicalRecord")
				// .content(tempMedicalRecordJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();

	}

	@Test
	final void testUpdateMedicalRecord() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int medicalRecordsSize_Before = mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ medicalRecordsSize_Before);

		MedicalRecord tempMedicalRecord = new MedicalRecord("Allison", "Boyd", "15/03/2018",
				Arrays.asList("medication1", "mdeciation2"),
				Arrays.asList("allergy1", "allergy2", "allergy3"));

		String tempMedicalRecordJson = mapper.writeValueAsString(tempMedicalRecord);

		assertEquals(false, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord));

		mvcResult = medicalRecordRestControllerMockMvc.perform(MockMvcRequestBuilders
				.put("/medicalRecord")
				.content(tempMedicalRecordJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn();

		assertEquals(0, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size() - medicalRecordsSize_Before);
		assertEquals(true, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord));

	}

	@Test
	final void testUpdateMedicalRecord_WithoutBody_isBadRequest() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int medicalRecordsSize_Before = mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ medicalRecordsSize_Before);

		mvcResult = medicalRecordRestControllerMockMvc.perform(MockMvcRequestBuilders
				.put("/medicalRecord")
				// .content(tempMedicalRecordJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();

	}

	@Test
	final void testUpdateMedicalRecord_NotExistingPerson_isNotFound() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int medicalRecordsSize_Before = mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ medicalRecordsSize_Before);

		MedicalRecord tempMedicalRecord = new MedicalRecord("blabla", "Boyd", "15/03/2018",
				Arrays.asList("medication1", "mdeciation2"),
				Arrays.asList("allergy1", "allergy2", "allergy3"));

		String tempMedicalRecordJson = mapper.writeValueAsString(tempMedicalRecord);

		assertEquals(false, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord));

		mvcResult = medicalRecordRestControllerMockMvc.perform(MockMvcRequestBuilders
				.put("/medicalRecord")
				.content(tempMedicalRecordJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();

		assertEquals(0, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size() - medicalRecordsSize_Before);
		assertEquals(false, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord));

	}

	@Test
	final void testDeleteMedicalRecord_WithGoodParamtersFirstNameLastName_isAccepted() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int medicalRecordsSize_Before = mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ medicalRecordsSize_Before);

		MedicalRecord tempMedicalRecord = new MedicalRecord("Brian", "Stelzer", "12/06/1975",
				Arrays.asList("ibupurin:200mg", "hydrapermazol:400mg"),
				Arrays.asList("nillacilan"));

		String tempMedicalRecordJson = mapper.writeValueAsString(tempMedicalRecord);

		assertEquals(true, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord));

		mvcResult = medicalRecordRestControllerMockMvc.perform(MockMvcRequestBuilders
				.delete("/medicalRecord?firstName=Brian&lastName=Stelzer")
				// .content(tempMedicalRecordJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size());

		assertEquals(false, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord));
		assertEquals(-1, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size() - medicalRecordsSize_Before);
	}

	@Test
	final void testDeleteMedicalRecord_NotExistingPerson_isNotFound() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int medicalRecordsSize_Before = mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "medicalRecordsSize : "
						+ medicalRecordsSize_Before);

		MedicalRecord tempMedicalRecord = new MedicalRecord("blabla", "Boyd", "15/03/2018",
				Arrays.asList("medication1", "mdeciation2"),
				Arrays.asList("allergy1", "allergy2", "allergy3"));

		String tempMedicalRecordJson = mapper.writeValueAsString(tempMedicalRecord);

		assertEquals(false, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord));

		mvcResult = medicalRecordRestControllerMockMvc.perform(MockMvcRequestBuilders
				.delete("/medicalRecord?firstName=blabla&lastName=Stelzer")
				// .content(tempMedicalRecordJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();

		assertEquals(0, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().size() - medicalRecordsSize_Before);
		assertEquals(false, mappingService.readJsonFile(JSON_FILE).getMedicalRecords().contains(tempMedicalRecord));

	}

}
