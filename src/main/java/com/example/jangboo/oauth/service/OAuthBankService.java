package com.example.jangboo.oauth.service;

import org.springframework.stereotype.Component;

import com.example.jangboo.oauth.client.OpenBankingClient;
import com.example.jangboo.oauth.controller.dto.response.AccountInfoResponse;
import com.example.jangboo.oauth.token.dto.TokenInfo;
import com.example.jangboo.oauth.token.service.TokenService;

@Component
public class OAuthBankService {
	private final OpenBankingClient client;
	private final TokenService tokenService;

	public OAuthBankService(OpenBankingClient client, TokenService tokenService) {
		this.client = client;
		this.tokenService = tokenService;
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
}
