package com.pilot.util;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {

	public static String getClientIp(HttpServletRequest request){
		
		String clientIp = request.getHeader("X-Forwarded-For");
		
		if(null == clientIp){
			clientIp = request.getHeader("Proxy-Client-IP");
		}
		
		if(null == clientIp){
			clientIp = request.getRemoteAddr();
		}
		
		return clientIp;
	}

	public static String getClientUserAgent(HttpServletRequest request){
		return request.getHeader("user-agent");
	}
}
