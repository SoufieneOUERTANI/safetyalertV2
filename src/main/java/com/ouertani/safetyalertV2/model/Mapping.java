package com.ouertani.safetyalertV2.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mapping {

	List<Person> persons;
	List<FireStation> firestations;
	List<MedicalRecord> medicalrecords;

}
