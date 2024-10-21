package com.example.jangboo.transaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.global.dto.ResultDto;
import com.example.jangboo.transaction.service.TransactionService;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@GetMapping("/balance")
	public ResponseEntity<ResultDto<String>> balance(
		@AuthenticationPrincipal CurrentUserInfo userInfo
	) {
		return ResponseEntity.ok(ResultDto.of(200,"잔액이 조회되었습니다.",transactionService.getLatestBalance(userInfo.userId())));
	}
}
