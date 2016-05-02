package com.pilot.validator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class WriteForm {

	@NotNull
	private String type;
	
	@NotNull
	@Size(min = 1, max = 2000)
	private String content;
	
	@NotNull
	@Size(min = 8, max = 20)
	private String password;
	
}
