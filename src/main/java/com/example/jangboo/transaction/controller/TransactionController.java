package com.example.jangboo.transaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.global.dto.ResultDto;
import com.example.jangboo.transaction.controller.dto.response.TransactionDetailResponse;
import com.example.jangboo.transaction.controller.dto.response.TransactionPageResponse;
import com.example.jangboo.transaction.controller.dto.response.TransactionsResponse;
import com.example.jangboo.transaction.service.TransactionService;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@GetMapping("/balance")
	public ResponseEntity<ResultDto<String>> getBalance(
		@AuthenticationPrincipal CurrentUserInfo userInfo
	) {
		return ResponseEntity.ok(
			ResultDto.of(200, "잔액이 조회되었습니다.", transactionService.getLatestBalance(userInfo.deptId())));
	}

	@GetMapping("/latest")
	public ResponseEntity<ResultDto<TransactionsResponse>> getLatestTransactions(
		@AuthenticationPrincipal CurrentUserInfo userInfo
	) {
		return ResponseEntity.ok(ResultDto.of(200, "최근 거래내역이 조회되었습니다.", transactionService.getTop5Transactions(
			userInfo.deptId())));
	}

	@GetMapping("/all/{pageNum}")
	public ResponseEntity<ResultDto<TransactionPageResponse>> getLatestTransactions(
		@AuthenticationPrincipal CurrentUserInfo userInfo,
		@PathVariable int pageNum,
		@RequestParam String fromDate,
		@RequestParam String toDate
	) {
		return ResponseEntity.ok(ResultDto.of(200, "거래내역 목록이 조회되었습니다", transactionService.getTransactionsByDate(
			fromDate,toDate,userInfo.deptId(),pageNum)));
	}

	@GetMapping("/payed")
	public ResponseEntity<ResultDto<TransactionsResponse>> getPayedListByName(
		@AuthenticationPrincipal CurrentUserInfo userInfo,
		@RequestParam String name
	) {
		return ResponseEntity.ok(ResultDto.of(200, "거래내역 목록이 조회되었습니다",transactionService.getPayedInfo(name,userInfo.deptId())));
	}

	@GetMapping("/notWrited/{pageNum}")
	public ResponseEntity<ResultDto<TransactionPageResponse>> getNotWrittenTransactions(
		@AuthenticationPrincipal CurrentUserInfo userInfo,
		@PathVariable int pageNum
	) {
		return ResponseEntity.ok(ResultDto.of(200, "거래내역 목록이 조회되었습니다",transactionService.getNonWriteTransactions(
			userInfo.deptId(),pageNum)));
	}

	@GetMapping("/detail")
	public ResponseEntity<ResultDto<TransactionDetailResponse>> getDetailTransaction(
		@AuthenticationPrincipal CurrentUserInfo userInfo,
		@RequestParam Long transactionId
	) {
		return ResponseEntity.ok(ResultDto.of(200, "거래내역 목록이 조회되었습니다",transactionService.getDetailTransaction(transactionId)));
	}
}
