package com.pilot.service;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.pilot.entity.User;
import com.pilot.model.UserModel;
import com.pilot.util.IPUtil;

@Service
public class RedisService {

	@Autowired
	private AuthorizeService authorizeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisTemplate<String, UserModel> template;
	
	public void addInfo(String key, UserModel user, HttpServletRequest request){
		ValueOperations<String, UserModel> ops = template.opsForValue();
		if (!template.hasKey(key)) {
			
			user.setUserAgent(IPUtil.getClientUserAgent(request));
			user.setClientIp(IPUtil.getClientIp(request));
			
			ops.set(key, user, 1, TimeUnit.HOURS);
		}
	}
	
	public User getUserInfoByKey(String key){
		User user = userService.findByEmail(template.opsForValue().get(key).getEmail());
		
		return user;
	}
	
	public void deleteUserInfoByKey(String key){
		if(template.hasKey(key)) {
			template.delete(key);
		}
	}
	
}
