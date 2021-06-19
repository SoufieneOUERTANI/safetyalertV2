package com.ouertani.safetyalertV2.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonPropertyOrder({ "AdultNum", "ChildNum", "personnes" })
public class GetFireStationClassReturn {
	int AdultNum;
	int ChildNum;
	List<GetFireStationClass> personnes;
}