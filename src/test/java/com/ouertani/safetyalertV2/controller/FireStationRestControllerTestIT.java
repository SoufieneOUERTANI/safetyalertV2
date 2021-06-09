package com.ouertani.safetyalertV2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouertani.safetyalertV2.constants.TestConstants;
import com.ouertani.safetyalertV2.dto.FileJsonMapping;
import com.ouertani.safetyalertV2.dto.GetFireStationClassReturn;
import com.ouertani.safetyalertV2.service.IMappingService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FireStationRestControllerTestIT {

	@Autowired
	IMappingService mappingService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc fireStationRestControllerMockMvc;

	@Before
	@Autowired
	public void setUp_MockMVC() {
		fireStationRestControllerMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testFireStationRestController() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testAddFireStation() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testUpdateFireStation() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testDeleteFireStation() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetFireStation_fireStationNumber_2() throws Exception {

		FileJsonMapping.mapping = mappingService.readJsonFile(TestConstants.JSON_TEST_FILE);

		MvcResult mvcResult;

		ObjectMapper mapper = new ObjectMapper();

		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/fireStation?fireStationNumber=2")
				// .content(Util.asJsonString(new Adress(0, "Adress_01", null)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
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

}
