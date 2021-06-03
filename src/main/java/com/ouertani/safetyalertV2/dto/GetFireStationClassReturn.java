package com.ouertani.safetyalertV2.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonPropertyOrder({ "AdultNum", "ChildNum", "personnes" })
public class GetFireStationClassReturn{
	int AdultNum;
	int ChildNum;
    //@JsonProperty("personnes")
	List<GetFireStationClass> personnes;
}