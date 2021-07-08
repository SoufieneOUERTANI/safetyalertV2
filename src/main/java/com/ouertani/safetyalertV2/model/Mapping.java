package com.ouertani.safetyalertV2.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class Mapping {

	List<Person> persons;
	List<FireStation> firestations;
	@JsonAlias("medicalrecords")
	List<MedicalRecord> medicalRecords;

}
