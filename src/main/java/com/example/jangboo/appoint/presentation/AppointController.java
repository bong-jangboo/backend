package com.example.jangboo.appoint.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jangboo.appoint.application.AppointService;
import com.example.jangboo.appoint.dto.AppointRequestDto;
import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.global.dto.ResultDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/appoint")
public class AppointController {

	private final AppointService appointService;

	@Operation(summary = "회장의 부회장 또는 총무 임명", tags = {"임명"})
	@PostMapping
	public ResponseEntity<ResultDto<String>> appointRole(@AuthenticationPrincipal CurrentUserInfo info,
		@RequestBody AppointRequestDto requestDto) {

		appointService.appointVicePresidentOrManager(requestDto, info.userId());

		return ResponseEntity.ok(ResultDto.of(200, requestDto.getRole() + " 임명 성공", null));
	}

}
