package com.example.jangboo.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${jangboo.front_end.url}")
	private String frontEndUrl;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
			.allowedOrigins(frontEndUrl)  // 프론트엔드 주소 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Preflight 요청 허용
			.allowedHeaders("*")
			.allowCredentials(true)
			.maxAge(3600);
	}
}
