package com.pilot.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pilot.service.AuthorizeService;
import com.pilot.service.CookieService;
import com.pilot.service.RedisService;

@Controller
public class AuthController {

	@Autowired
	private AuthorizeService authorizeService;
	
	@Autowired
	private RedisService redisService;

	@Autowired
	private CookieService cookieService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@RequestMapping("/oauth2_callback")
	public String oauth(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response){
		
		String token = authorizeService.getToken(code);
		authorizeService.checkUser(token, request);
		
		cookieService.createTeamIdCookie(request, response);
		cookieService.createTokenCookie(response, token);
		
		return "redirect:/list?page=1";
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response){
		
		Cookie tmIdCookie = cookieService.getCookie(request, "tmid");
		Cookie tokenCookie = cookieService.getCookie(request, "tk");
		
		String email = authorizeService.getMyInform(tokenCookie.getValue()).getEmail();
		redisService.deleteUserInfoByEmail(email);
		
		cookieService.removeCookie(response, tmIdCookie);
		cookieService.removeCookie(response, tokenCookie);
		
		return "redirect:/login";
	}
	
	@RequestMapping(value = "/auth/error", method = RequestMethod.GET)
	public String error(){
		return "error/error";
	}
	
}
