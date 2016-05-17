package com.pilot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.pilot.entity.User;

@Service
public class RedisService {

	@Autowired
	AuthorizeService authorizeService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private RedisTemplate<String, String> template;
	
	public void addInfo(String token, String String){
		ValueOperations<String, String> ops = template.opsForValue();
		if (!template.hasKey(token)) {
			ops.set(token, String);
		}
	}
	
	public User getUserInfoByKey(String key){
		ValueOperations<String, String> ops = template.opsForValue();
		User user = userService.findByEmail(authorizeService.getMyInform(ops.get(key)).getEmail());
		
		return user;
	}
	
	public void deleteUserInfoByKey(String key){
		ValueOperations<String, String> ops = template.opsForValue();
		if(template.hasKey(key)) {
			template.delete(key);
		}
	}
	
}
