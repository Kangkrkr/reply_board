package com.pilot.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RoomModel implements Serializable {

	@JsonProperty("room")
	Integer room;
	
}
