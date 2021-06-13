package com.ouertani.safetyalertV2.controller;

import static org.junit.Assert.assertTrue;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouertani.safetyalertV2.dto.MedicalDetailsPersonsAdress;
import com.ouertani.safetyalertV2.model.ID;
import com.ouertani.safetyalertV2.model.Person;
import com.ouertani.safetyalertV2.service.IMappingService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonRestControllerTest {

	private static final Logger logger = LogManager.getLogger("PersonRestController");

	@Value("${JSON_FILE}")
	private String JSON_FILE;

	@Value("${JSON_FILE_SAVE}")
	private String JSON_FILE_SAVE;

	@Autowired
	IMappingService mappingService;

	@Autowired
	private MockMvc personRestControllerMockMvc;

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
//	final void testPersonRestController() {
//		fail("Not yet implemented"); // TODO
//	}

	@Test
	final void testPostPerson_NotAlreadyExist_isCreated() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int personsSize_Before = mappingService.readJsonFile(JSON_FILE).getPersons().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "personsSize : "
						+ personsSize_Before);

		Person tempPerson = new Person("firstName_1", "lastName_1", null, null, null, null, null);

		assertEquals(mappingService.readJsonFile(JSON_FILE).getPersons().contains(tempPerson), false);

		String tempPersonJson = mapper.writeValueAsString(tempPerson);

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.post("/person")
				.content(tempPersonJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "personsSize : "
						+ mappingService.readJsonFile(JSON_FILE).getPersons().size());

		assertEquals(mappingService.readJsonFile(JSON_FILE).getPersons().contains(tempPerson), true);
		assertEquals(1, mappingService.readJsonFile(JSON_FILE).getPersons().size() - personsSize_Before);

	}

	@Test
	final void testPostPerson_AlreadyExist_isConflict() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int personsSize_Before = mappingService.readJsonFile(JSON_FILE).getPersons().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "personsSize : "
						+ personsSize_Before);

		Person tempPerson = new Person("Allison", "Boyd", null, null, null, null, null);

		List<ID> mappingPersonID = mappingService.readJsonFile(JSON_FILE).getPersons().stream()
				.map(c -> new ID(c.getFirstName(), c.getLastName())).collect(Collectors.toList());

		logger.debug("mappingPersonID : " + mappingPersonID);

		ID thePersonID = new ID(tempPerson.getFirstName(), tempPerson.getLastName());

		logger.debug("thePersonID : " + thePersonID);

		assertEquals(true, mappingPersonID.contains(thePersonID));

		String tempPersonJson = mapper.writeValueAsString(tempPerson);

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.post("/person")
				.content(tempPersonJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andReturn();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "personsSize : "
						+ mappingService.readJsonFile(JSON_FILE).getPersons().size());

		// assertEquals(false,
		// mappingService.readJsonFile(JSON_FILE).getPersons().contains(tempPerson));
		assertEquals(0, mappingService.readJsonFile(JSON_FILE).getPersons().size() - personsSize_Before);

	}

	@Test
	final void testUpdatePerson() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int personsSize_Before = mappingService.readJsonFile(JSON_FILE).getPersons().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "personsSize : "
						+ personsSize_Before);

		Person tempPerson = new Person("Foster", "Shepard", "New Adress", "Culver", "97451", "841-874-6544",
				"jaboyd@email.com");

		String tempPersonJson = mapper.writeValueAsString(tempPerson);

		assertEquals(mappingService.readJsonFile(JSON_FILE).getPersons().contains(tempPerson), false);

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.put("/person?personAdress=1509 Culver St")
				.content(tempPersonJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn();

		assertEquals(0, mappingService.readJsonFile(JSON_FILE).getPersons().size() - personsSize_Before);
		assertEquals(mappingService.readJsonFile(JSON_FILE).getPersons().contains(tempPerson), true);

	}

	@Test
	final void testDeletePerson_WithGoodParamtersFirstNameLastName_isAccepted() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int personsSize_Before = mappingService.readJsonFile(JSON_FILE).getPersons().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "personsSize : "
						+ personsSize_Before);

		// firstName=Foster, lastName=Shepard, address=748 Townings Dr, city=Culver,
		// zip=97451, phone=841-874-6544, email=jaboyd@email.com

		Person tempPerson = new Person("Foster", "Shepard", "748 Townings Dr", "Culver", "97451", "841-874-6544",
				"jaboyd@email.com");

		String tempPersonJson = mapper.writeValueAsString(tempPerson);

		assertEquals(true, mappingService.readJsonFile(JSON_FILE).getPersons().contains(tempPerson));

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.delete("/person?firstName=Foster&lastName=Shepard")
				.content(tempPersonJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "personsSize : "
						+ mappingService.readJsonFile(JSON_FILE).getPersons().size());

		assertEquals(false, mappingService.readJsonFile(JSON_FILE).getPersons().contains(tempPerson));
		assertEquals(-1, mappingService.readJsonFile(JSON_FILE).getPersons().size() - personsSize_Before);
	}

	@Test
	final void testGetChildrenAdress_WithRightParametersValues_isFound() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/childAlert?address=1509 Culver St")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				// .andExpect(jsonPath("$.personnes").exists())
				.andReturn();

		logger.debug("mvcResult.getResponse().getContentAsString() : " + mvcResult.getResponse().getContentAsString());
		List<Person> readValue = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<Person>>() {
				});

		assertEquals(2, readValue.size());

		Person tempPerson;
		String tempJasonPerson;

		tempJasonPerson = "			{\r\n"
				+ "	        \"firstName\": \"Tenley\",\r\n"
				+ "	        \"lastName\": \"Boyd\",\r\n"
				+ "	        \"address\": \"1509 Culver St\",\r\n"
				+ "	        \"city\": \"Culver\",\r\n"
				+ "	        \"zip\": \"97451\",\r\n"
				+ "	        \"phone\": \"841-874-6512\",\r\n"
				+ "	        \"email\": \"tenz@email.com\"\r\n"
				+ "			}";

		tempPerson = mapper.readValue(tempJasonPerson, Person.class);
		assertEquals(true, readValue.contains(tempPerson));

		tempJasonPerson = "{\r\n"
				+ "	        \"firstName\": \"Roger\",\r\n"
				+ "	        \"lastName\": \"Boyd\",\r\n"
				+ "	        \"address\": \"1509 Culver St\",\r\n"
				+ "	        \"city\": \"Culver\",\r\n"
				+ "	        \"zip\": \"97451\",\r\n"
				+ "	        \"phone\": \"841-874-6512\",\r\n"
				+ "	        \"email\": \"jaboyd@email.com\"\r\n"
				+ "			}";
		tempPerson = mapper.readValue(tempJasonPerson, Person.class);
		assertEquals(true, readValue.contains(tempPerson));

	}

	@Test
	final void testGetChildrenAdress_WithBadParameters_isBadRequest() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/childAlert?badParameter=1509 Culver St")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				// .andExpect(jsonPath("$.personnes").exists())
				.andReturn();

	}

	@Test
	final void testGetChildrenAdress_WithBadParametersValues_isNotFound() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/childAlert?address=1509 Culver St__")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				// .andExpect(jsonPath("$.personnes").exists())
				.andReturn();

	}

	@Test
	final void testGetChildrenAdress_WithRightParametersValues_NoChidren_isNotFound() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/childAlert?address=908 73rd St")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				// .andExpect(jsonPath("$.personnes").exists())
				.andReturn();

	}

	@Test
	// http://localhost:8080/fire?address=<address>
	final void testGetMedicalDetailsPersonsAdress_WithRightParametersValues_isFound() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/fire?address=1509 Culver St")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				// .andExpect(jsonPath("$.personnes").exists())
				.andReturn();

		List<MedicalDetailsPersonsAdress> readValue = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<MedicalDetailsPersonsAdress>>() {
				});
		logger.debug("SOUE77 : " + readValue);

		assertEquals(readValue.size(), 5);

		// On vérifiera que 2 valeurs, on peut compléter par la suite
		assertTrue(readValue.contains(new MedicalDetailsPersonsAdress("3", "John", "Boyd", "841-874-6512", 37,
				Arrays.asList("aznol:350mg", "hydrapermazol:100mg"), java.util.Arrays.asList("nillacilan"))));
		assertTrue(readValue.contains(new MedicalDetailsPersonsAdress("3", "Felicia", "Boyd", "841-874-6544", 35,
				Arrays.asList("tetracyclaz:650mg"), java.util.Arrays.asList("xilliathal"))));

	}

	@Test
	final void testGetMedicalDetailsPersonsAdress_WithBadParameters_isBadRequest() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/fire?badParameter=1509 Culver St")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				// .andExpect(jsonPath("$.personnes").exists())
				.andReturn();

	}

	@Test
	final void testGetMedicalDetailsPersonsAdress_WithBadParametersValues_isNotFound() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/fire?address=1509 Culver St___")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				// .andExpect(jsonPath("$.personnes").exists())
				.andReturn();

	}

	@Test
	// http://localhost:8080/communityEmail?city=<city>
	final void testGetEmailsCity_WithRightParametersValues_isFound() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/communityEmail?city=Culver")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				// .andExpect(jsonPath("$.personnes").exists())
				.andReturn();

		List<String> readValue = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<String>>() {
				});
		logger.debug("SOUE77 : " + readValue);

		assertEquals(15, readValue.size());

		assertTrue(readValue.contains("aly@imail.com"));
		assertTrue(readValue.contains("bstel@email.com"));
		assertTrue(readValue.contains("clivfd@ymail.com"));
		assertTrue(readValue.contains("drk@email.com"));
		assertTrue(readValue.contains("gramps@email.com"));
		assertTrue(readValue.contains("jaboyd@email.com"));
		assertTrue(readValue.contains("jpeter@email.com"));
		assertTrue(readValue.contains("lily@email.com"));
		assertTrue(readValue.contains("reg@email.com"));
		assertTrue(readValue.contains("soph@email.com"));
		assertTrue(readValue.contains("ssanw@email.com"));
		assertTrue(readValue.contains("tcoop@ymail.com"));
		assertTrue(readValue.contains("tenz@email.com"));
		assertTrue(readValue.contains("ward@email.com"));
		assertTrue(readValue.contains("zarc@email.com"));

	}

	@Test
	final void testGetEmailsCity_WithBadParameters_isBadRequest() throws Exception {

		personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/communityEmail?badParameter=Culver")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				// .andExpect(jsonPath("$.personnes").exists())
				.andReturn();

	}

	@Test
	final void testGetEmailsCity_WithBadParametersValues_isNotFound() throws Exception {

		personRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/communityEmail?city=1509 Culver St___")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				// .andExpect(jsonPath("$.personnes").exists())
				.andReturn();

	}

}
