package com.pilot.service;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ChatService {

	public String getRoomList(String token){
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("https://edge.tmup.com/v1/rooms");
		uriBuilder.queryParam("token", token);;
		
		URI roomUri = uriBuilder.build().encode().toUri();
		
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(roomUri, String.class);
		
		return result;
	}
	
}
