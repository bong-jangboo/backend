package com.bongjangboo.legacy.auth.controller;

import com.bongjangboo.legacy.auth.controller.dto.request.LoginRequest;
import com.bongjangboo.legacy.auth.controller.dto.response.JwtToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bongjangboo.legacy.auth.service.AuthService;
import com.bongjangboo.legacy.global.dto.ResultDto;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<ResultDto<JwtToken>> login(
		@RequestBody LoginRequest request
	) throws Exception {
		return ResponseEntity.ok(ResultDto.of(200,"로그인 성공",authService.getAuthentication(request)));
	}
}
