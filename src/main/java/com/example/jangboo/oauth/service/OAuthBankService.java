package com.example.jangboo.oauth.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

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
		return client.getAccountInfo(tokenInfo.userSeqNo(),tokenInfo.accessToken()).getBody();
	}

	public MockTransactionResponse getTransactions(Long userId) throws Exception {
		TokenInfo tokenInfo = tokenService.getTokenInfoByUserId(userId);
		TransactionRequest request = getTransactionRequestInfo(userId);

		return client.getTransactions(tokenInfo.accessToken(), request).getBody();
	}

	private TransactionRequest getTransactionRequestInfo(Long userId) {
		return new TransactionRequest(
			"1234",
			accountService.getFintechUseNum(userId),
			LocalDateToString(transactionService.findLatestUpdatedTransactionDate(userId)),
			LocalDateToString(LocalDate.now())
		);
	}

	private String LocalDateToString(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return date.format(formatter);
	}

}
