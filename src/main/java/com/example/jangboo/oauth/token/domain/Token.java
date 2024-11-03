package com.example.jangboo.oauth.token.domain;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@RedisHash(value = "open_banking_token", timeToLive = 36000)
public class Token implements Serializable {
	@Id
	private Long id;

	private String accessToken;
	private String refreshToken;
	private String userSeqNo;

	@Indexed
	private Long ownerId;

	@Builder
	public Token(Long id, String accessToken, String refreshToken, String userSeqNo, Long ownerId) {
		this.id = id;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.userSeqNo = userSeqNo;
		this.ownerId = ownerId;
	}

	public void refresh(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
