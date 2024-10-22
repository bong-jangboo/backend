package com.example.jangboo.oauth.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.jangboo.account.service.AccountService;
import com.example.jangboo.oauth.client.OpenBankingClient;
import com.example.jangboo.oauth.client.request.TransactionRequest;
import com.example.jangboo.oauth.client.response.AccountInfoResponse;
import com.example.jangboo.oauth.client.response.MockTransactionResponse;
import com.example.jangboo.oauth.token.dto.TokenInfo;
import com.example.jangboo.oauth.token.service.TokenService;
import com.example.jangboo.transaction.service.TransactionService;

@Component
public class OAuthBankService {
	private final OpenBankingClient client;
	private final TokenService tokenService;
	private final AccountService accountService;
	private final TransactionService transactionService;

	public OAuthBankService(OpenBankingClient client, TokenService tokenService, AccountService accountService,
		TransactionService transactionService) {
		this.client = client;
		this.tokenService = tokenService;
		this.accountService = accountService;
		this.transactionService = transactionService;
	}

	public String getAuthUrl(Long userId){
		return client.getAuthUrl(userId);
	}

	public Void getAccessToken(String code,Long userId) throws Exception {
		return tokenService.createTokenInfo(client.requestAccessToken(code),userId);
	}

	public AccountInfoResponse getAccountInfo(Long userId) throws Exception {
		TokenInfo tokenInfo = tokenService.getTokenInfoByUserId(userId);
		AccountInfoResponse response = client.getAccountInfo(tokenInfo.userSeqNo(),tokenInfo.accessToken()).getBody();

		return response;
	}

	public void getTransactions(Long userId) throws Exception {
		TokenInfo tokenInfo = tokenService.getTokenInfoByUserId(userId);
		TransactionRequest request = getTransactionRequestInfo(userId);

		transactionService.saveTransactions(Optional.ofNullable(
			client.getTransactions(tokenInfo.accessToken(), request).getBody()
		).orElseThrow(() -> new IllegalStateException("거래 내역이 업데이트 되지 않았습니다.")),userId);
	}

	private TransactionRequest getTransactionRequestInfo(Long userId) {
		LocalDateTime latestDateTime = transactionService.findLatestUpdatedTransactionDateTime(userId);
		return new TransactionRequest(
			"1234",
			accountService.getFintechUseNum(userId),
			LocalDateToString(latestDateTime.toLocalDate()),
			LocalDateToString(LocalDate.now()),
			LocalTimeToString(latestDateTime.toLocalTime().plusSeconds(1)),
			LocalTimeToString(LocalDateTime.now().toLocalTime())
		);
	}

	private String LocalDateToString(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.format(formatter);
	}

	private String LocalTimeToString(LocalTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(formatter);
	}

	public boolean isVerified(Long userId) throws Exception {
		return (tokenService.getTokenInfoByUserId(userId).accessToken() != null)&&(
			accountService.isNotExist(userId)
			);
	}
}
