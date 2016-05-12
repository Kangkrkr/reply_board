package com.pilot.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class JoinForm {
	
	@NotNull
	@Size(min = 10, max = 40)
	private String email;
	
	@NotNull
	@Size(min = 2, max = 10)
	private String name;
	
	@NotNull
	@Size(min = 8, max = 120)
	private String password;
}
