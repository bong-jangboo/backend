package com.example.legacy.oauth.controller;

import com.example.legacy.oauth.client.response.AccountInfoResponse;
import com.example.legacy.oauth.service.OAuthBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.legacy.auth.controller.dto.Info.CurrentUserInfo;
import com.example.legacy.auth.controller.dto.response.JwtToken;
import com.example.legacy.auth.service.AuthService;
import com.example.legacy.global.dto.ResultDto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/oauth")
public class OAuthBankController {
	private final OAuthBankService oAuthBankService;
	private final AuthService authService;

	@Autowired
	public OAuthBankController(OAuthBankService oAuthBankService, AuthService authService) {
		this.oAuthBankService = oAuthBankService;
		this.authService = authService;
	}

	@GetMapping("/open-bank")
	public String redirectToOpenBank(@AuthenticationPrincipal CurrentUserInfo userInfo) {
		return oAuthBankService.getAuthUrl(userInfo.userId());
	}

	@GetMapping("/token")
	public RedirectView getOpenBankToken(
		@RequestParam String code,
		@RequestParam String scope,
		@RequestParam String state,
		@RequestParam("client_info") Long userId,
		HttpServletResponse response
	) throws Exception {
		oAuthBankService.getAccessToken(code,userId);

		JwtToken token = authService.getNewJwt(userId);

		Cookie jwtCookie = new Cookie("accessToken", token.accessToken());
		jwtCookie.setHttpOnly(true);
		jwtCookie.setMaxAge(60 * 60); // 1시간 동안 유효
		jwtCookie.setPath("/");      // 전체 경로에서 사용 가능
		response.addCookie(jwtCookie);

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:5500/");
		return redirectView;
	}

	@GetMapping("/account-list")
	public ResponseEntity<ResultDto<AccountInfoResponse>> getAccountList(@AuthenticationPrincipal CurrentUserInfo userInfo) throws
		Exception {
		return ResponseEntity.ok(ResultDto.of(200,"유저의 계좌정보리스트가 조회되었습니다.",oAuthBankService.getAccountInfo(
			userInfo.userId())));
	}

	@GetMapping("/transactions")
	public ResponseEntity<ResultDto<Void>>getTransactions(@AuthenticationPrincipal CurrentUserInfo userInfo) throws
		Exception {
		oAuthBankService.getTransactions(userInfo.userId(),userInfo.deptId());
		return ResponseEntity.ok(ResultDto.of(200,"유저의 거래내역이 업데이트 되었습니다.",null));
	}

	@GetMapping("/is-verified")
	public ResponseEntity<Boolean> isVerified(@AuthenticationPrincipal CurrentUserInfo userInfo) throws
		Exception {
		return ResponseEntity.ok(oAuthBankService.isVerified(userInfo.userId()));
	}
}
