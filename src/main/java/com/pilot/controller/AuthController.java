package com.pilot.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pilot.entity.User;
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
	private HttpSession session;
	
	private static final Logger logger  = org.slf4j.LoggerFactory.getLogger(AuthController.class);
	
	// 실제 인증을 수행하게 되는 곳.
	// Authorization Server에서 authorization code를 담아 여기로 redirecting 하게됨.
	// 여기서 code를 받아 다시 Authorization Server로 부터 code를 기반으로 token을 얻는다.
	@RequestMapping("/oauth2_callback")
	public String oauth(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response){
		
		String token = authorizeService.getToken(code);
		UserModel user = authorizeService.getMyInform(token);
		
		if(null == userService.findByEmail(user.getEmail())){
			User newer = new User();
			newer.setEmail(user.getEmail());
			newer.setName(user.getName());
			newer.setProfileImage(user.getProfileImage());
			
			userService.join(newer);
		}

		/* 쿠키에 token key를 저장.
		CookieGenerator cookieGenerator = new CookieGenerator();
		cookieGenerator.setCookieName("key");
		cookieGenerator.addCookie(response, token);
		*/
		
		// 쿠키에 token key를 저장. 쿠키는 5초간 살아있음.
		System.err.println("로그인 한 사람 : " + authorizeService.getMyInform(token).toString());
		Cookie cookie = new Cookie("token", token);
		System.err.println(token);
		cookie.setMaxAge(60 * 60);
		response.addCookie(cookie);
		
		//session.setAttribute("userInfo", user);
		
		return "redirect:/list?page=1";
	}
	
}
