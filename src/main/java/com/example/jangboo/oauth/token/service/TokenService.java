package com.example.jangboo.oauth.token.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jangboo.oauth.token.domain.Token;
import com.example.jangboo.oauth.token.domain.TokenRepository;
import com.example.jangboo.oauth.token.dto.TokenInfo;

@Service
public class TokenService {
	private final TokenEncryptor tokenEncryptor;
	private final TokenRepository tokenRepository;

	@Autowired
	public TokenService(TokenEncryptor tokenEncryptor, TokenRepository tokenRepository) {
		this.tokenEncryptor = tokenEncryptor;
		this.tokenRepository = tokenRepository;
	}

	@Transactional
	public Void createTokenInfo(TokenInfo tokenInfo,Long userId) throws Exception {
		System.out.println(tokenInfo.userSeqNo()+" "+tokenInfo.accessToken());
		tokenRepository.save(
			Token.builder()
				.accessToken(getEncryptedToken(tokenInfo.accessToken()))
				.refreshToken(getEncryptedToken(tokenInfo.refreshToken()))
				.userSeqNo(tokenInfo.userSeqNo())
				.ownerId(userId)
				.build()
		);
		return null;
	}

	private String getEncryptedToken(String token) throws Exception {
		return tokenEncryptor.encrypt(token);
	}

	private String getDecryptedToken(String token) throws Exception {
		return tokenEncryptor.decrypt(token);
	}

	public TokenInfo getTokenInfoByUserId(Long userId) throws Exception {
		Token token = tokenRepository.findByOwnerId(userId);
		return new TokenInfo(getDecryptedToken(token.getAccessToken()),getDecryptedToken(token.getRefreshToken()),token.getUserSeqNo());
	}

	@Transactional
	public void refreshTokens(Long userId, TokenInfo tokenInfo) {
		Token token = tokenRepository.findByOwnerId(userId);

		token.refresh(tokenInfo.accessToken(),tokenInfo.refreshToken());
	}
}
