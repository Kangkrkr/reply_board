package com.pilot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamModel {
	
	@JsonProperty("index")
	int idx;
	
	@JsonProperty("name")
	String name;
	
}