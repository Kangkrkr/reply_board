package com.pilot.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pilot.model.UserModel;
import com.pilot.service.AuthorizeService;
import com.pilot.service.UserService;

@Controller
public class AuthController {

	@Autowired
	private AuthorizeService authorizeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisTemplate<String, UserModel> template;
	
	// 실제 인증을 수행하게 되는 곳.
	// Authorization Server에서 authorization code를 담아 여기로 redirecting 하게됨.
	// 여기서 code를 받아 다시 Authorization Server로 부터 code를 기반으로 token을 얻는다.
	@RequestMapping("/oauth2_callback")
	public String oauth(@RequestParam("code") String code, 
			@RequestHeader("user-agent") String userAgent,
			HttpServletRequest request,
			HttpServletResponse response){
		
		// 크롬 : Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36
		// 엣지 : Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240
		// 익스플로러 : Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko
		String ipAddress = request.getHeader("X-FORWARDED-FOR");	// PROXY SERVER 또는 LOAD BALANCER를 거쳐나온 클라이언트의 IP
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();	// 보편적인 클라이언트 IP를 수집하는 방법
		}
		System.err.println(ipAddress);
		System.err.println(userAgent);
		
		String token = authorizeService.getToken(code);
		authorizeService.checkUser(token);
		
		ValueOperations<String, UserModel> ops = template.opsForValue();
		String key = "token";
		if (!this.template.hasKey(key)) {
			ops.set(key, authorizeService.getMyInform(token));
		}
		System.err.println("Found - " + key + ", value=" + ops.get(key));

		Cookie cookie = new Cookie("token", token);
		cookie.setMaxAge(60);
		response.addCookie(cookie);

		return "redirect:/list?page=1";
	}
	
}
