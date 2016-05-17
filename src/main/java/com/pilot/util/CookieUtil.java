package com.pilot.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

	// 쿠키 리스트를 맵으로 변환.
	public static Cookie getCookie(HttpServletRequest request, String key){
		
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		
		for(Cookie cookie : request.getCookies()) {
			String cookieName = cookie.getName();
			
			cookieMap.put(cookieName, cookie);
		}
		
		return cookieMap.get(key);
	}
}
