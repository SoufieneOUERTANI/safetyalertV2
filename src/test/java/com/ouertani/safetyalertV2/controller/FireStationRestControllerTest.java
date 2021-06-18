package com.ouertani.safetyalertV2.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

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
import com.ouertani.safetyalertV2.dto.FileJsonMapping;
import com.ouertani.safetyalertV2.dto.GetFireStationClassReturn;
import com.ouertani.safetyalertV2.dto.ListPersonsDetailsListStatioNumber;
import com.ouertani.safetyalertV2.model.FireStation;
import com.ouertani.safetyalertV2.service.IMappingService;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FireStationRestControllerTest {

	private static final Logger logger = LogManager.getLogger("FireStationRestController");

	@Value("${JSON_FILE}")
	private String JSON_FILE;

	@Value("${JSON_FILE_SAVE}")
	private String JSON_FILE_SAVE;

	@Autowired
	IMappingService mappingService;

	@Autowired
	private MockMvc fireStationRestControllerMockMvc;

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
//	final void testFireStationRestController() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	final void testAddFireStation() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	final void testUpdateFireStation() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	final void testDeleteFireStation() {
//		fail("Not yet implemented"); // TODO
//	}

	@Test
	final void testGetFireStation_stationNumber_2_isFound() throws Exception {

		FileJsonMapping.mapping = mappingService.readJsonFile(JSON_FILE);

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/firestation?stationNumber=2")
				// .content(Util.asJsonString(new Adress(0, "Adress_01", null)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.childNum").exists())
				.andExpect(jsonPath("$.childNum").value("1"))
				.andExpect(jsonPath("$.adultNum").exists())
				.andExpect(jsonPath("$.adultNum").value("4"))
				.andExpect(jsonPath("$.personnes").exists())
				.andExpect(jsonPath("$.personnes[0].firstName").exists())
				.andExpect(jsonPath("$.personnes[1].firstName").exists())
				.andExpect(jsonPath("$.personnes[2].firstName").exists())
				.andExpect(jsonPath("$.personnes[3].firstName").exists())
				.andExpect(jsonPath("$.personnes[4].firstName").exists())
				.andExpect(jsonPath("$.personnes[0].firstName").value("Jonanathan"))
				.andExpect(jsonPath("$.personnes[1].firstName").value("Sophia"))
				.andExpect(jsonPath("$.personnes[2].firstName").value("Warren"))
				.andExpect(jsonPath("$.personnes[3].firstName").value("Zach"))
				.andExpect(jsonPath("$.personnes[4].firstName").value("Eric"))
				.andExpect(jsonPath("$.personnes[0].lastName").exists())
				.andExpect(jsonPath("$.personnes[1].lastName").exists())
				.andExpect(jsonPath("$.personnes[2].lastName").exists())
				.andExpect(jsonPath("$.personnes[3].lastName").exists())
				.andExpect(jsonPath("$.personnes[4].lastName").exists())
				.andExpect(jsonPath("$.personnes[0].lastName").value("Marrack"))
				.andExpect(jsonPath("$.personnes[1].lastName").value("Zemicks"))
				.andExpect(jsonPath("$.personnes[2].lastName").value("Zemicks"))
				.andExpect(jsonPath("$.personnes[3].lastName").value("Zemicks"))
				.andExpect(jsonPath("$.personnes[4].lastName").value("Cadigan"))
				.andExpect(jsonPath("$.personnes[0].adress").exists())
				.andExpect(jsonPath("$.personnes[1].adress").exists())
				.andExpect(jsonPath("$.personnes[2].adress").exists())
				.andExpect(jsonPath("$.personnes[3].adress").exists())
				.andExpect(jsonPath("$.personnes[4].adress").exists())
				.andExpect(jsonPath("$.personnes[0].adress").value("29 15th St"))
				.andExpect(jsonPath("$.personnes[1].adress").value("892 Downing Ct"))
				.andExpect(jsonPath("$.personnes[2].adress").value("892 Downing Ct"))
				.andExpect(jsonPath("$.personnes[3].adress").value("892 Downing Ct"))
				.andExpect(jsonPath("$.personnes[4].adress").value("951 LoneTree Rd"))
				.andExpect(jsonPath("$.personnes[0].phone").exists())
				.andExpect(jsonPath("$.personnes[1].phone").exists())
				.andExpect(jsonPath("$.personnes[2].phone").exists())
				.andExpect(jsonPath("$.personnes[3].phone").exists())
				.andExpect(jsonPath("$.personnes[4].phone").exists())
				.andExpect(jsonPath("$.personnes[0].phone").value("841-874-6513"))
				.andExpect(jsonPath("$.personnes[1].phone").value("841-874-7878"))
				.andExpect(jsonPath("$.personnes[2].phone").value("841-874-7512"))
				.andExpect(jsonPath("$.personnes[3].phone").value("841-874-7512"))
				.andExpect(jsonPath("$.personnes[4].phone").value("841-874-7458"))
				// .andExpect(header().string("Location", "blabla"))
				.andReturn();

		GetFireStationClassReturn readValue = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				GetFireStationClassReturn.class);

		assertEquals(5, readValue.getPersonnes().size());

		net.minidev.json.JSONArray jsonArray = com.jayway.jsonpath.JsonPath
				.read(mvcResult.getResponse().getContentAsString(), "$.personnes[*]");
		assertEquals(5, jsonArray.size());

	}

	@Test
	final void testGetFireStation_stationNumber_C_BadRequest() throws Exception {

		FileJsonMapping.mapping = mappingService.readJsonFile(JSON_FILE);

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/firestation?stationNumber=C")
				// .content(Util.asJsonString(new Adress(0, "Adress_01", null)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				// .andExpect(header().string("Location", "blabla"))
				.andReturn();

	}

	@Test
	final void testPostFireStation_AlreadyExist_isConflict() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		FireStation tempFireStation = new FireStation("1509 Culver St", "3");

		String tempFireStationJson = mapper.writeValueAsString(tempFireStation);

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.post("/firestation")
				.content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andReturn();
	}

	@Test
	final void testPostFireStation_NotAlreadyExist_isCreated() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int firestationsSize_Before = mappingService.readJsonFile(JSON_FILE).getFirestations().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "firestationsSize : "
						+ firestationsSize_Before);

		FireStation tempFireStation = new FireStation("NouvelleAdresse", "3");

		assertEquals(mappingService.readJsonFile(JSON_FILE).getFirestations().contains(tempFireStation), false);

		String tempFireStationJson = mapper.writeValueAsString(tempFireStation);

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.post("/firestation")
				.content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "firestationsSize : "
						+ mappingService.readJsonFile(JSON_FILE).getFirestations().size());

		assertEquals(mappingService.readJsonFile(JSON_FILE).getFirestations().contains(tempFireStation), true);
		assertEquals(1, mappingService.readJsonFile(JSON_FILE).getFirestations().size() - firestationsSize_Before);

	}

	@Test
	final void testDeleteFireStation_WithoutParamters_isBadRequest() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		FireStation tempFireStation = new FireStation("1509 Culver St", "3");

		String tempFireStationJson = mapper.writeValueAsString(tempFireStation);

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.delete("/firestation")
				.content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();
	}

	@Test
	final void testDeleteFireStation_WithGoodParamters_isAccepted() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		FireStation tempFireStation = new FireStation("1509 Culver St", "3");

		String tempFireStationJson = mapper.writeValueAsString(tempFireStation);

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.delete("/firestation?fireStationAdress=1509 Culver St&fireStationNumber=3")
				.content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn();
	}

	@Test
	final void testDeleteFireStation_WithBadParamtersValues_isNotFound() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		FireStation tempFireStation = new FireStation("1509 Culver St", "3");

		String tempFireStationJson = mapper.writeValueAsString(tempFireStation);

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.delete("/firestation?fireStationAdress=1509 Culver St___&fireStationNumber=3")
				.content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	final void testDeleteFireStation_WithGoodParamterFireStationNumber_isAccepted() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int firestationsSize_Before = mappingService.readJsonFile(JSON_FILE).getFirestations().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "firestationsSize : "
						+ firestationsSize_Before);

		FireStation tempFireStation = new FireStation("1509 Culver St", "3");

		String tempFireStationJson = mapper.writeValueAsString(tempFireStation);

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.delete("/firestation?stationNumber=3")
				.content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "firestationsSize : "
						+ mappingService.readJsonFile(JSON_FILE).getFirestations().size());

		assertEquals(-5, mappingService.readJsonFile(JSON_FILE).getFirestations().size() - firestationsSize_Before);

	}

	@Test
	final void testDeleteFireStation_WithGoodParamterfireStationAdress_isAccepted() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int firestationsSize_Before = mappingService.readJsonFile(JSON_FILE).getFirestations().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "firestationsSize : "
						+ firestationsSize_Before);

		FireStation tempFireStation = new FireStation("1509 Culver St", "3");

		String tempFireStationJson = mapper.writeValueAsString(tempFireStation);

		assertEquals(mappingService.readJsonFile(JSON_FILE).getFirestations().contains(tempFireStation), true);

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.delete("/firestation?fireStationAdress=1509 Culver St")
				.content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "firestationsSize : "
						+ mappingService.readJsonFile(JSON_FILE).getFirestations().size());

		assertEquals(mappingService.readJsonFile(JSON_FILE).getFirestations().contains(tempFireStation), false);
		assertEquals(-1, mappingService.readJsonFile(JSON_FILE).getFirestations().size() - firestationsSize_Before);
	}

	@Test
	final void testUpdateFireStation_WithGoodParamterfireStationAdress_isAccepted() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		int firestationsSize_Before = mappingService.readJsonFile(JSON_FILE).getFirestations().size();

		logger.debug(
				"mapping : " + mappingService.readJsonFile(JSON_FILE) + "/" + "firestationsSize : "
						+ firestationsSize_Before);

		FireStation tempFireStation = new FireStation("1509 Culver St", "22");

		String tempFireStationJson = mapper.writeValueAsString(tempFireStation);

		assertEquals(mappingService.readJsonFile(JSON_FILE).getFirestations().contains(tempFireStation), false);

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.put("/firestation?fireStationAdress=1509 Culver St")
				.content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn();

		assertEquals(0, mappingService.readJsonFile(JSON_FILE).getFirestations().size() - firestationsSize_Before);
		assertEquals(mappingService.readJsonFile(JSON_FILE).getFirestations().contains(tempFireStation), true);

	}

	@Test
	final void testUpdateFireStation_WithBadParamterValuefireStationAdress_isNotFound() throws Exception {

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		FireStation tempFireStation = new FireStation("1509 Culver St__", "22");

		String tempFireStationJson = mapper.writeValueAsString(tempFireStation);

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.put("/firestation?fireStationAdress=1509 Culver St")
				.content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
	}

	@Test
	final void testgetPersonsPhoneFireStation_isFound() throws Exception {
		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/phoneAlert?firestation=2")
				// .content()
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				.andReturn();

		List<String> readValue = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<String>>() {
				});
		assertEquals(4, readValue.size());
		assertEquals(true, readValue.contains("841-874-7458"));
		assertEquals(true, readValue.contains("841-874-7878"));
		assertEquals(true, readValue.contains("841-874-7512"));
		assertEquals(true, readValue.contains("841-874-7458"));
	}

	@Test
	final void testgetPersonsPhoneFireStation_13_isNotFound() throws Exception {
		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/phoneAlert?firestation=13")
				// .content()
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();

	}

	@Test
	final void testListListPersonsDetailsListStatioNumber_WithGoodParamterfireStationAdress_isFound()
			throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		MvcResult mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/flood/stations?stations=1&stations=4")
				// .content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				.andReturn();

		List<ListPersonsDetailsListStatioNumber> readValue = mapper.readValue(
				mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<ListPersonsDetailsListStatioNumber>>() {
				});

		assertEquals(5, readValue.size());

		assertTrue(readValue.contains(mapper.readValue("    {\r\n"
				+ "        \"adress\": \"644 Gershwin Cir\",\r\n"
				+ "        \"listPersonsDetailsListStatioNumber\": [\r\n"
				+ "            {\r\n"
				+ "                \"firstName\": \"Peter\",\r\n"
				+ "                \"lastName\": \"Duncan\",\r\n"
				+ "                \"phone\": \"841-874-6512\",\r\n"
				+ "                \"age\": 20,\r\n"
				+ "                \"medications\": [],\r\n"
				+ "                \"allergies\": [\r\n"
				+ "                    \"shellfish\"\r\n"
				+ "                ]\r\n"
				+ "            }\r\n"
				+ "        ]\r\n"
				+ "    }", ListPersonsDetailsListStatioNumber.class)));

		assertTrue(readValue.contains(mapper.readValue("   {\r\n"
				+ "        \"adress\": \"908 73rd St\",\r\n"
				+ "        \"listPersonsDetailsListStatioNumber\": [\r\n"
				+ "            {\r\n"
				+ "                \"firstName\": \"Reginold\",\r\n"
				+ "                \"lastName\": \"Walker\",\r\n"
				+ "                \"phone\": \"841-874-8547\",\r\n"
				+ "                \"age\": 41,\r\n"
				+ "                \"medications\": [\r\n"
				+ "                    \"thradox:700mg\"\r\n"
				+ "                ],\r\n"
				+ "                \"allergies\": [\r\n"
				+ "                    \"illisoxian\"\r\n"
				+ "                ]\r\n"
				+ "            },\r\n"
				+ "            {\r\n"
				+ "                \"firstName\": \"Jamie\",\r\n"
				+ "                \"lastName\": \"Peters\",\r\n"
				+ "                \"phone\": \"841-874-7462\",\r\n"
				+ "                \"age\": 39,\r\n"
				+ "                \"medications\": [],\r\n"
				+ "                \"allergies\": []\r\n"
				+ "            }\r\n"
				+ "        ]\r\n"
				+ "    }", ListPersonsDetailsListStatioNumber.class)));
		assertTrue(readValue.contains(mapper.readValue("    {\r\n"
				+ "        \"adress\": \"489 Manchester St\",\r\n"
				+ "        \"listPersonsDetailsListStatioNumber\": [\r\n"
				+ "            {\r\n"
				+ "                \"firstName\": \"Lily\",\r\n"
				+ "                \"lastName\": \"Cooper\",\r\n"
				+ "                \"phone\": \"841-874-9845\",\r\n"
				+ "                \"age\": 27,\r\n"
				+ "                \"medications\": [],\r\n"
				+ "                \"allergies\": []\r\n"
				+ "            }\r\n"
				+ "        ]\r\n"
				+ "    }", ListPersonsDetailsListStatioNumber.class)));
		assertTrue(readValue.contains(mapper.readValue("{\r\n"
				+ "        \"adress\": \"112 Steppes Pl\",\r\n"
				+ "        \"listPersonsDetailsListStatioNumber\": [\r\n"
				+ "            {\r\n"
				+ "                \"firstName\": \"Tony\",\r\n"
				+ "                \"lastName\": \"Cooper\",\r\n"
				+ "                \"phone\": \"841-874-6874\",\r\n"
				+ "                \"age\": 27,\r\n"
				+ "                \"medications\": [\r\n"
				+ "                    \"hydrapermazol:300mg\",\r\n"
				+ "                    \"dodoxadin:30mg\"\r\n"
				+ "                ],\r\n"
				+ "                \"allergies\": [\r\n"
				+ "                    \"shellfish\"\r\n"
				+ "                ]\r\n"
				+ "            },\r\n"
				+ "            {\r\n"
				+ "                \"firstName\": \"Ron\",\r\n"
				+ "                \"lastName\": \"Peters\",\r\n"
				+ "                \"phone\": \"841-874-8888\",\r\n"
				+ "                \"age\": 56,\r\n"
				+ "                \"medications\": [],\r\n"
				+ "                \"allergies\": []\r\n"
				+ "            },\r\n"
				+ "            {\r\n"
				+ "                \"firstName\": \"Allison\",\r\n"
				+ "                \"lastName\": \"Boyd\",\r\n"
				+ "                \"phone\": \"841-874-9888\",\r\n"
				+ "                \"age\": 56,\r\n"
				+ "                \"medications\": [\r\n"
				+ "                    \"aznol:200mg\"\r\n"
				+ "                ],\r\n"
				+ "                \"allergies\": [\r\n"
				+ "                    \"nillacilan\"\r\n"
				+ "                ]\r\n"
				+ "            }\r\n"
				+ "        ]\r\n"
				+ "    }", ListPersonsDetailsListStatioNumber.class)));

	}

	@Test
	final void testListListPersonsDetailsListStatioNumber_15_isNotFound() throws Exception {

		fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/flood/stations?stations=1&stations=15")
				// .content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				.andReturn();

	}

	@Test
	final void testListListPersonsDetailsListStatioNumber_WrongParameter_isNotFound() throws Exception {

		fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/flood/stations?badparameter=1&stations=15")
				// .content(tempFireStationJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();

	}

}
