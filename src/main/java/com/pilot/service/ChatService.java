package com.pilot.service;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pilot.model.MessageModel;
import com.pilot.model.RoomModel;
import com.pilot.model.UserModel;
import com.pilot.util.TeamUp;

@Service
public class ChatService {

	@Autowired
	private RedisService redisService;
	
	@Autowired
	private UserModel bot;
	
	private RestTemplate restTemplate = new RestTemplate();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
	
	public Integer createRoom(String email){
		
		UserModel creator = redisService.getUserModelByEmail(email);
		
		Map<String, Object> params = new HashMap<>();
		params.put("users", Arrays.asList(bot.getId(), creator.getId()));
		
		String body = getBodyFromMapper(params);
		
		Integer room = null;
		if(body != null) {
			
			// 김설현 bot이 있는 1번팀을 기준으로 ..
			HttpEntity entity = new HttpEntity(body, configHeaders(bot));
			ResponseEntity<RoomModel> result = restTemplate.postForEntity(buildURI(TeamUp.ROOM) + "{team}", entity, RoomModel.class, 1);
			
			room = result.getBody().getRoom();
		}
		
		return room;
	}

	public int sendMessageToRoom(String message, int room) {

		Map<String, Object> params = new HashMap<>();
		params.put("content", message);
		
		String body = getBodyFromMapper(params);
		
		ResponseEntity<MessageModel> result = null;
		if(body != null) {
			HttpEntity entity = new HttpEntity(body, configHeaders(bot));
			result = restTemplate.postForEntity(buildURI(TeamUp.MESSAGE) + "{room}", entity, MessageModel.class, room);
		}
		
		return result.getBody().getMsg();
	}

	private HttpHeaders configHeaders(UserModel bot) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "bearer " + bot.getToken());
		headers.add("Content-Type", "application/json; charset=utf-8");
		return headers;
	}

	private URI buildURI(String uri) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri);
		URI targetUri = uriBuilder.build().encode().toUri();
		return targetUri;
	}
	
	private String getBodyFromMapper(Map<String, Object> params) {
		
		String body = null;
		try{
			body = objectMapper.writeValueAsString(params);
			logger.info(body);
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
		
		return body;
	}
}
