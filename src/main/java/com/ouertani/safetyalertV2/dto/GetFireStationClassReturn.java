package com.ouertani.safetyalertV2.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({ "AdultNum", "ChildNum", "personnes" })
public class GetFireStationClassReturn {
	int AdultNum;
	int ChildNum;
	List<GetFireStationClass> personnes;
}