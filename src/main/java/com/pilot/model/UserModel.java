package com.pilot.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements Serializable {
	
	// 필요 정보 : 이름, email, profile image 
	@JsonProperty("index")
	private int id;
	
	@JsonProperty("name")
	private String name; 
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("profile_image")
	private String profileImage;
	
	private String userAgent;
	private String clientIp;
}
