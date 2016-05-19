package com.pilot.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pilot.model.UserModel;
import com.pilot.service.AuthorizeService;
import com.pilot.service.CookieService;
import com.pilot.util.IPUtil;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private AuthorizeService authorizeService;
	
	@Autowired
	private CookieService cookieService;
	
	@Autowired
	private RedisTemplate<String, UserModel> template;
	
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	
	// 모든 URL로의 request에 대해 다음의 Intercepter를 적용한다.
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RequestHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/auth/error");
	}

	class RequestHandlerInterceptor extends HandlerInterceptorAdapter {
		
		// 컨트롤러에 요청을 전달하기 전에 preHandle() 메서드를 이용해 유효성 검증.
		// 반환값이 true면 지정한 컨트롤러로 넘기고, false라면 넘기지 않는다.
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

			Cookie tokenCookie = cookieService.getCookie(request, "tk");
			Cookie tmIdCookie = cookieService.getCookie(request, "tmid");
			
			if(tokenCookie != null && tmIdCookie != null) {
				String email = authorizeService.getMyInform(tokenCookie.getValue()).getEmail();
				String key = DigestUtils.sha256Hex(email);
				
				if(template.hasKey(key)) {
					String srcIP = IPUtil.getClientIp(request);
					String srcUA = IPUtil.getClientUserAgent(request);
					
					String destIP = template.opsForValue().get(key).getClientIp();
					String destUA =  template.opsForValue().get(key).getUserAgent();
					
					if(!compareIP(srcIP, destIP) || !compareUA(srcUA, destUA)) {
						logger.warn(srcIP + " 로부터 올바르지 못한 접근이 감지 되었습니다.");
						cookieService.removeSpecifiedCookies(response, tokenCookie, tmIdCookie);
						response.sendRedirect("/auth/error");
					}
				}
			}
			
			return true;
		}
		
		/* 컨트롤러로 요청이 보내지고 난 후.
		@Override
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		}
		*/
		
		public boolean compareIP(String src, String dest) {
			return dest.equals(src);
		}
		
		public boolean compareUA(String src, String dest) {
			return dest.equals(src);
		}
	}
}
