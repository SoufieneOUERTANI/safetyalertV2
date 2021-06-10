/**
 * 
 */
package com.ouertani.safetyalertV2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouertani.safetyalertV2.dto.GetFireStationClassReturn;
import com.ouertani.safetyalertV2.model.MedicalRecord;
import com.ouertani.safetyalertV2.model.Person;
import com.ouertani.safetyalertV2.service.IFireStationService;
import com.ouertani.safetyalertV2.service.IMedicalRecordService;
import com.ouertani.safetyalertV2.service.IPersonService;

/**
 * @author SOUE
 *
 */

@WebMvcTest(controllers = FireStationRestController.class)
class FireStationRestControllerTest {

	@Autowired
	private MockMvc fireStationRestControllerMockMvc;

	@MockBean
	private IPersonService personService;

	@MockBean
	private IFireStationService fireStationService;

	@MockBean
	private IMedicalRecordService medicalRecordService;

	/**
	 * Test method for
	 * {@link com.ouertani.safetyalertV2.controller.FireStationRestController#getFireStation(java.lang.String)}.
	 * 
	 * @throws Exception
	 */
	@Test
	final void testGetFireStation_Mock_fireStationNumber_2_isFound() throws Exception {

		when(fireStationService.getAdressFireStation(anyString())).thenReturn(Arrays.asList("Adress1", "Adress2"));

		when(personService.getPersonAdress("Adress1")).thenReturn(
				Arrays.asList(new Person("firstName1", "lastName1", "Adress1", "City1", "Zip1", "Phone1", "Mail1"),
						new Person("firstName2", "lastName2", "Adress2", "City2", "Zip2", "Phone2", "Mail2"),
						new Person("firstName3", "lastName3", "Adress3", "City3", "Zip3", "Phone3", "Mail3")));

		when(medicalRecordService.getMedicalRecordPerson("firstName1", "lastName1"))
				.thenReturn(new MedicalRecord("firstName1", "lastName1", "02/03/2001",
						Arrays.asList("Maladie1", "Maladie2"), Arrays.asList("Alergy1", "Alergy2")));
		when(medicalRecordService.getMedicalRecordPerson("firstName2", "lastName2"))
				.thenReturn(new MedicalRecord("firstName2", "lastName2", "02/03/2005",
						Arrays.asList("Maladie3", "Maladie4"), Arrays.asList("Alergy3", "Alergy4")));
		when(medicalRecordService.getMedicalRecordPerson("firstName3", "lastName3"))
				.thenReturn(new MedicalRecord("firstName3", "lastName3", "02/03/2005",
						Arrays.asList("Maladie5", "Maladie6"), Arrays.asList("Alergy5", "Alergy6")));

		MvcResult mvcResult;
		mvcResult = fireStationRestControllerMockMvc.perform(MockMvcRequestBuilders
				.get("/fireStation?fireStationNumber=2")
				// .content(Util.asJsonString(new Adress(0, "Adress_01", null)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.childNum").exists())
				.andExpect(jsonPath("$.childNum").value("2"))
				.andExpect(jsonPath("$.adultNum").exists())
				.andExpect(jsonPath("$.adultNum").value("1"))
				.andExpect(jsonPath("$.personnes").exists())
				.andExpect(jsonPath("$.personnes[0].firstName").exists())
				.andExpect(jsonPath("$.personnes[1].firstName").exists())
				.andExpect(jsonPath("$.personnes[2].firstName").exists())
				.andExpect(jsonPath("$.personnes[0].firstName").value("firstName1"))
				.andExpect(jsonPath("$.personnes[1].firstName").value("firstName2"))
				.andExpect(jsonPath("$.personnes[2].firstName").value("firstName3"))
				.andExpect(jsonPath("$.personnes[0].lastName").exists())
				.andExpect(jsonPath("$.personnes[1].lastName").exists())
				.andExpect(jsonPath("$.personnes[2].lastName").exists())
				.andExpect(jsonPath("$.personnes[0].lastName").value("lastName1"))
				.andExpect(jsonPath("$.personnes[1].lastName").value("lastName2"))
				.andExpect(jsonPath("$.personnes[2].lastName").value("lastName3"))
				.andExpect(jsonPath("$.personnes[0].adress").exists())
				.andExpect(jsonPath("$.personnes[1].adress").exists())
				.andExpect(jsonPath("$.personnes[2].adress").exists())
				.andExpect(jsonPath("$.personnes[0].adress").value("Adress1"))
				.andExpect(jsonPath("$.personnes[1].adress").value("Adress2"))
				.andExpect(jsonPath("$.personnes[2].adress").value("Adress3"))
				.andExpect(jsonPath("$.personnes[0].phone").exists())
				.andExpect(jsonPath("$.personnes[1].phone").exists())
				.andExpect(jsonPath("$.personnes[2].phone").exists())
				.andExpect(jsonPath("$.personnes[0].phone").value("Phone1"))
				.andExpect(jsonPath("$.personnes[1].phone").value("Phone2"))
				.andExpect(jsonPath("$.personnes[2].phone").value("Phone3"))
				// .andExpect(header().string("Location", "/api/account/12345"))
				.andReturn();

		ObjectMapper mapper = new ObjectMapper();
		GetFireStationClassReturn readValue = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				GetFireStationClassReturn.class);
		assertEquals(3, readValue.getPersonnes().size());

		net.minidev.json.JSONArray jsonArray = com.jayway.jsonpath.JsonPath
				.read(mvcResult.getResponse().getContentAsString(), "$.personnes[*]");
		assertEquals(3, jsonArray.size());

	}

}
