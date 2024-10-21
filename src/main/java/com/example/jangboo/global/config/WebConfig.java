package com.example.jangboo.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:5500")  // 프론트엔드 주소 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Preflight 요청 허용
			.allowedHeaders("*")  // Authorization 허용
			.allowCredentials(true);
	}
}
