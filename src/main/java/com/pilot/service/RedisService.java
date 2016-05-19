package com.pilot.service;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.pilot.entity.User;
import com.pilot.model.UserModel;
import com.pilot.util.Bot;

@Service
public class RedisService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisTemplate<String, UserModel> template;
	
	@Bean
	public UserModel bot() {
		return getUserModelByEmail(Bot.BOT_ACCOUNT);
	}
	
	public void addInfo(String email, UserModel user, HttpServletRequest request){
		ValueOperations<String, UserModel> ops = template.opsForValue();
		
		// 봇의 정보가 redis에 저장되있는지 확인.
		if(Bot.BOT_ACCOUNT.equals(user.getEmail()) && !template.hasKey(Bot.BOT_ACCOUNT)) {
			ops.set(Bot.BOT_ACCOUNT, user, 2, TimeUnit.DAYS);
		}else{
			if (!template.hasKey(email)) {
				//user.setUserAgent(IPUtil.getClientUserAgent(request));
				//user.setClientIp(IPUtil.getClientIp(request));
				ops.set(email, user, 1, TimeUnit.HOURS);
			}
		}
	}
	
	public User getUserByEmail(String email){
		User user = userService.findByEmail(email);
		
		return user;
	}
	
	public UserModel getUserModelByEmail(String email){
		UserModel user = template.opsForValue().get(email);
		return user;
	}
	
	public void deleteUserInfoByEmail(String email){
		if(template.hasKey(email)) {
			template.delete(email);
		}
	}
	
}
