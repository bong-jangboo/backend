package com.example.legacy.oauth.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.legacy.oauth.client.request.TransactionRequest;
import com.example.legacy.oauth.client.response.AccountInfoResponse;
import com.example.legacy.oauth.client.response.MockTransactionResponse;
import com.example.legacy.oauth.client.response.OpenBankingTokenResponse;
import com.example.legacy.oauth.client.response.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.legacy.oauth.token.dto.TokenInfo;

@Component
public class OpenBankingClient {
	private static final String AUTH_GRANT_TYPE = "authorization_code";
	private static final String REFRESH_GRANT_TYPE = "refresh_token";

	@Value("${oauth.open-banking.uri.token-uri}")
	private String tokenUrl;

	@Value("${oauth.open-banking.key.client-id}")
	private String clientId;

	@Value("${oauth.open-banking.key.client-secret}")
	private String clientSecret;

	@Value("${oauth.open-banking.uri.redirect-uri}")
	private String redirectUri;

	@Value("${oauth.open-banking.scope}")
	private String scope;

	@Value("${oauth.open-banking.uri.user_info-uri}")
	private String userInfoUri;

	@Value("${mock.uri}")
	private String mockUri;

	private final RestTemplate restTemplate;

	@Autowired
	public OpenBankingClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public TokenInfo requestAccessToken(String authorizationCode) {
		MultiValueMap<String, String> body = createAccessTokenRequestBody(authorizationCode);
		return makeTokenRequest(body);
	}

	public TokenInfo refreshAccessToken(String refreshToken) {
		MultiValueMap<String, String> body = createRefreshTokenRequestBody(refreshToken);
		return makeTokenRequest(body);
	}

	private TokenInfo makeTokenRequest(MultiValueMap<String, String> body) {
		String url = tokenUrl;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<?> request = new HttpEntity<>(body, headers);

		OpenBankingTokenResponse response = restTemplate.postForObject(url, request, OpenBankingTokenResponse.class);
		assert response != null;
		return new TokenInfo(response.getAccessToken(), response.getRefreshToken(), response.getUserSeqNo());
	}

	private MultiValueMap<String, String> createAccessTokenRequestBody(String authorizationCode) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("code", authorizationCode);
		body.add("grant_type", AUTH_GRANT_TYPE);
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("redirect_uri", redirectUri);
		return body;
	}

	private MultiValueMap<String, String> createRefreshTokenRequestBody(String refreshToken) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("refresh_token", refreshToken);
		body.add("grant_type", REFRESH_GRANT_TYPE);
		body.add("scope", scope);
		return body;
	}

	public String getAuthUrl(Long userId) {
		return "https://testapi.openbanking.or.kr/oauth/2.0/authorize?response_type=code&client_id="+
			clientId+"&redirect_uri="+redirectUri+"&scope="+scope+"&client_info="+userId+"&state=12345678901234567890123456789012&auth_type=0";
	}

	public ResponseEntity<AccountInfoResponse> getAccountInfo(String userSeqNo, String accessToken) {
		String accountInfoUrl = "https://testapi.openbanking.or.kr/v2.0/account/list";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);  // Access Token 추가
		headers.setContentType(MediaType.APPLICATION_JSON);

		String urlTemplate = UriComponentsBuilder.fromHttpUrl(accountInfoUrl)
			.queryParam("user_seq_no", userSeqNo)
			.queryParam("include_cancel_yn", "N")
			.queryParam("sort_order", "A")
			.encode()
			.toUriString();

		HttpEntity<?> entity = new HttpEntity<>(headers);

		return restTemplate.exchange(
			urlTemplate,
			HttpMethod.GET,
			entity,
			AccountInfoResponse.class
		);
	}

	public ResponseEntity<MockTransactionResponse> getTransactions(String accessToken, TransactionRequest request) {
		String accountInfoUrl = "https://openapi.openbanking.or.kr/v2.0/account/transaction_list/fin_num";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);  // Access Token 추가
		headers.setContentType(MediaType.APPLICATION_JSON);

		String urlTemplate = UriComponentsBuilder.fromHttpUrl(accountInfoUrl)
			.queryParam("bank_tran_id", request.bankTranId())
			.queryParam("fintech_use_num", request.fintechUseNum())
			.queryParam("inquiry_type", "A")
			.queryParam("inquiry_base", "D")
			.queryParam("from_date", request.fromDate())
			.queryParam("to_date", request.toDate())
			.queryParam("tran_dtime", DateTimeFormatter(LocalDate.now()))
			.encode()
			.toUriString();

		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<TransactionResponse> response = restTemplate.exchange(
			urlTemplate,
			HttpMethod.GET,
			entity,
			TransactionResponse.class
		);

		return getMockTransactions(request);
	}

	public ResponseEntity<MockTransactionResponse> getMockTransactions(TransactionRequest request) {
		String accountInfoUrl = mockUri;

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(accountInfoUrl)
			.queryParam("account_id", request.fintechUseNum())
			.queryParam("start_date", request.fromDate())
			.queryParam("end_date", request.toDate())
			.queryParam("start_time",request.fromTime())
			.queryParam("end_time",request.toTime());

		try {
			return restTemplate.getForEntity(builder.toUriString(), MockTransactionResponse.class);
		} catch (HttpServerErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new RuntimeException("최근 거래내역이 없습니다.");
			}
			throw e;
		}
	}

	private String DateTimeFormatter(LocalDate date){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return date.format(formatter);
	}
}
