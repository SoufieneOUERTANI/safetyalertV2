package com.ouertani.safetyalertV2.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListPersonsDetailsListStatioNumber {
	private String adress;
	private List<PersonsDetailsListStatioNumber> listPersonsDetailsListStatioNumber;
}
