package com.pilot.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Configuration
public class JavaConfig {

	// ServerProperties 클래스 문서를 참고함.
	@Bean
	public ServerProperties serverProperties() {
		ServerProperties properties = new ServerProperties();
		properties.setPort(8010);

		return properties;
	}

	// 타임리프의 뷰리졸버를 가져와 기본설정에서 cache 타입만 바꾸어 반환.
	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setCache(false);
		return viewResolver;
	}

}
