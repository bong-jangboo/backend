package com.example.legacy.delegate.presentation;

import com.example.legacy.delegate.application.DelegateService;
import com.example.legacy.delegate.dto.DelegateRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.legacy.auth.controller.dto.Info.CurrentUserInfo;
import com.example.legacy.global.dto.ResultDto;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/delegate")
public class DelegateController {

	private final DelegateService delegateService;

	@Operation(summary = "위임하기", tags = {"위임"})
	@PostMapping
	public ResponseEntity<ResultDto<String>> mandateRole(
		@AuthenticationPrincipal CurrentUserInfo info,
		@RequestBody DelegateRequestDto requestDto) {

		delegateService.mandateRole(requestDto, info.userId());

		return ResponseEntity.ok(ResultDto.of(200, "위임하기 성공", null));
	}
}
