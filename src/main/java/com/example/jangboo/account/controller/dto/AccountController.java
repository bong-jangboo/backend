package com.example.jangboo.account.controller.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jangboo.account.service.AccountService;
import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.global.dto.ResultDto;

@RestController
@RequestMapping("/api/account")
public class AccountController {
	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/register")
	public ResponseEntity<ResultDto<Void>> registerAccount(
		@AuthenticationPrincipal CurrentUserInfo userInfo,
		@RequestParam(name="fintech_use_num") String fintechUseNum
	){
		accountService.registerAccount(userInfo.userId(),fintechUseNum);
		return ResponseEntity.ok(ResultDto.of(200,"계좌가 정상적으로 등록되었습니다.",null));
	}
}
