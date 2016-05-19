package com.pilot.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class CookieService {

	private static final Logger logger = LoggerFactory.getLogger(CookieService.class);
	
	public void createTokenCookie(HttpServletResponse response, String token) {
		Cookie tokenCookie = new Cookie("tk", token);
		tokenCookie.setHttpOnly(true);
		tokenCookie.setMaxAge(60 * 60);
		tokenCookie.setDomain(".tmup.com");
		tokenCookie.setPath("/");
		response.addCookie(tokenCookie);
	}

	public void createTeamIdCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie tmIdCookie = getCookie(request, "tmid");
		tmIdCookie.setHttpOnly(true);
		tmIdCookie.setMaxAge(60 * 60);
		tmIdCookie.setDomain(".tmup.com");
		tmIdCookie.setPath("/");
		response.addCookie(tmIdCookie);
	}
	
	public void removeCookie(HttpServletResponse response, Cookie target) {
		target.setMaxAge(0);
		target.setDomain(".tmup.com");
		target.setPath("/");          
		response.addCookie(target);
	}
	
	public void removeSpecifiedCookies(HttpServletResponse response, Cookie... cookies){
		for(Cookie c : cookies) {
			removeCookie(response, c);
		}
	}
	
	// 쿠키 리스트를 맵으로 변환.
	public Cookie getCookie(HttpServletRequest request, String key){
		
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		
		try{
			Cookie[] cookies = request.getCookies();
			
			for(Cookie cookie : cookies) {
				String cookieName = cookie.getName();
				
				cookieMap.put(cookieName, cookie);
			}
			
			return cookieMap.get(key);
		}catch(NullPointerException e){
			logger.error("찾으려는 쿠키가 존재하지 않습니다.", e.toString());
			return null;
		}
	}
}
