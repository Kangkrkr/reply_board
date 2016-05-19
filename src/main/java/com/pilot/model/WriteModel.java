package com.pilot.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class WriteModel {

	@NotNull
	private String type;
	
	@NotNull
	@Size(min = 1, max = 2000)
	private String content;
	
}
