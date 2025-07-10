package com.bongjangboo.legacy.oauth.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class OpenBankingTokenResponse {
	@Getter
	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

	@Getter
	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("expires_in")
	private String expiresIn;

	@JsonProperty("scope")
	private String scope;

	@Getter
	@JsonProperty("user_seq_no")
	private String userSeqNo;

	public OpenBankingTokenResponse() {
	}

}
