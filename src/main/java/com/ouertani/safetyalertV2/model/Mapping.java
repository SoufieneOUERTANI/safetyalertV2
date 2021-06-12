package com.ouertani.safetyalertV2.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mapping {

	List<Person> persons;
	List<FireStation> firestations;
	@JsonAlias("medicalrecords")
	List<MedicalRecord> medicalRecords;

}
