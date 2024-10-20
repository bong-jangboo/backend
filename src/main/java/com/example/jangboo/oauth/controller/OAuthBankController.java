package com.example.jangboo.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.global.dto.ResultDto;
import com.example.jangboo.oauth.client.response.AccountInfoResponse;
import com.example.jangboo.oauth.client.response.MockTransactionResponse;
import com.example.jangboo.oauth.service.OAuthBankService;

@RestController
@RequestMapping("/api/oauth")
public class OAuthBankController {
	private final OAuthBankService oAuthBankService;

	@Autowired
	public OAuthBankController(OAuthBankService oAuthBankService) {
		this.oAuthBankService = oAuthBankService;
	}

	@GetMapping("/open-bank")
	public String redirectToOpenBank(@AuthenticationPrincipal CurrentUserInfo userInfo) {
		return oAuthBankService.getAuthUrl(userInfo.userId());
	}

	@GetMapping("/token")
	public ResponseEntity<ResultDto<Void>> getOpenBankToken(
		@RequestParam String code,
		@RequestParam String scope,
		@RequestParam String state,
		@RequestParam("client_info") Long userId
	) throws Exception {
		return ResponseEntity.ok(ResultDto.of(200,"토큰 발급이 완료되었습니다.", oAuthBankService.getAccessToken(code,userId)));
	}

	@GetMapping("/account-list")
	public ResponseEntity<ResultDto<AccountInfoResponse>> getAccountList(@AuthenticationPrincipal CurrentUserInfo userInfo) throws
		Exception {
		return ResponseEntity.ok(ResultDto.of(200,"유저의 계좌정보리스트가 조회되었습니다.",oAuthBankService.getAccountInfo(
			userInfo.userId())));
	}

	@GetMapping("/transactions")
	public ResponseEntity<ResultDto<MockTransactionResponse>> getTransactions(@AuthenticationPrincipal CurrentUserInfo userInfo) throws
		Exception {
		return ResponseEntity.ok(ResultDto.of(200,"유저의 거래내역이 조회되었습니다.",oAuthBankService.getTransactions(
			userInfo.userId())));
	}
}
