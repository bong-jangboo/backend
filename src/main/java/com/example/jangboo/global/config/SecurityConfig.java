package com.example.jangboo.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.jangboo.global.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	@Autowired
	private JwtAuthenticationFilter jwtfiler;  // JWT 필터 주입

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors->cors.disable())
			.csrf(csrf -> csrf.disable())// CSRF 보호 비활성화
			.authorizeHttpRequests(auth -> auth
					// 어드민 로그인은 토큰 발급을 위해 항상 허용
					.requestMatchers("/admin/login").permitAll()
					// 그 외 어드민 경로는 인증 필요 (Nginx에서 IP 차단 수행 예정)
					.requestMatchers("/admin/**").authenticated()

				.requestMatchers("/api/univ/register/**","api/auth/login","/api/oauth/**","/api/role/main","/api/account/**","/api/transaction/**").permitAll()
				.requestMatchers("/api/univ/signup-link").hasAnyRole("AUDITOR","PRESIDENT")
				.anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 사용 안 함
			.addFilterBefore(jwtfiler, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
