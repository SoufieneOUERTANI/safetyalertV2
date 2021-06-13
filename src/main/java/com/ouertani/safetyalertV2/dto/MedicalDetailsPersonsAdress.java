package com.ouertani.safetyalertV2.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalDetailsPersonsAdress {

	private String station;

	private String firstName;

	private String lastName;

	private String phone;

	private int age;

	private List<String> medications;

	private List<String> allergies;

}
