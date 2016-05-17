package com.pilot.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pilot.service.AuthorizeService;
import com.pilot.service.RedisService;
import com.pilot.util.CookieUtil;

@Controller
public class AuthController {

	@Autowired
	private AuthorizeService authorizeService;
	
	@Autowired
	private RedisService redisService;
	
	
	// 실제 인증을 수행하게 되는 곳.
	// Authorization Server에서 authorization code를 담아 여기로 redirecting 하게됨.
	// 여기서 code를 받아 다시 Authorization Server로 부터 code를 기반으로 token을 얻는다.
	@RequestMapping("/oauth2_callback")
	public String oauth(@RequestParam("code") String code, @RequestHeader("user-agent") String userAgent,
												HttpServletRequest request, HttpServletResponse response){
		
		// key탈취 대비 user-agent , ip 체크 등의 유효성 검사 필요
		String ipAddress = request.getHeader("X-FORWARDED-FOR");	// PROXY SERVER 또는 LOAD BALANCER를 거쳐나온 클라이언트의 IP
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();	// 보편적인 클라이언트 IP를 수집하는 방법
		}
		
		Cookie tmIdCookie = CookieUtil.getCookie(request, "tmid");
		tmIdCookie.setHttpOnly(true);
		tmIdCookie.setMaxAge(60 * 60);
		tmIdCookie.setDomain(".tmup.com");
		tmIdCookie.setPath("/");
		response.addCookie(tmIdCookie);
		
		String token = authorizeService.getToken(code);
		authorizeService.checkUser(tmIdCookie.getValue(), token);

		return "redirect:/list?page=1";
	}
	
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response){
		
		// 로그아웃시 다음의 쿠키를 삭제한다. 삭제시 Domain과 Path를 지정해주지 않으면 삭제가 되지 않는다.
		Cookie tmIdCookie = CookieUtil.getCookie(request, "tmid");
		tmIdCookie.setMaxAge(0);
		tmIdCookie.setDomain(".tmup.com");
		tmIdCookie.setPath("/");          
		response.addCookie(tmIdCookie);
		
		redisService.deleteUserInfoByKey(tmIdCookie.getValue());
		
		return "redirect:/login";
		
	}
}
