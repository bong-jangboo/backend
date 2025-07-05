package com.example.legacy.oauth.token.dto;

public record TokenInfo(
	String accessToken,
	String refreshToken,
	String userSeqNo
) {
}
