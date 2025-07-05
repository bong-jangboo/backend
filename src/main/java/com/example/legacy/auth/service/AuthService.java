package com.example.legacy.auth.service;

import com.example.legacy.auth.JwtTokenProvider;
import com.example.legacy.auth.controller.dto.request.LoginRequest;
import com.example.legacy.auth.controller.dto.response.JwtToken;
import com.example.legacy.auth.domain.user.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.example.legacy.auth.domain.user.CustomUserDetailsService;

@Service
public class AuthService {
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private AuthenticationManager authenticationManager;

	public JwtToken getAuthentication(LoginRequest request) throws Exception {
		try {
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.loginId(), request.password())
			);
		} catch (AuthenticationException e) {
			throw new Exception("Incorrect loginId or password", e);
		}

		final CustomUserDetails userDetails = (CustomUserDetails)customUserDetailsService.loadUserByUsername(
			request.loginId());
		final JwtToken jwt = jwtTokenProvider.generateToken(userDetails);
		return jwt;
	}

	public JwtToken getNewJwt(Long userId){
		final CustomUserDetails userDetails = (CustomUserDetails)customUserDetailsService.loadUserById(userId);
		return jwtTokenProvider.generateToken(userDetails);
	}




}
