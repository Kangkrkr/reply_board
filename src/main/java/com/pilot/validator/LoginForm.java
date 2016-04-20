package com.pilot.validator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginForm {
	
	@NotNull
	@Size(min = 10, max = 127)
	private String email;
	
	@NotNull
	@Size(min = 8, max = 127)
	private String password;
}
