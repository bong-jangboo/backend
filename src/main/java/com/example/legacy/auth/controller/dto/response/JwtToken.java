package com.example.legacy.auth.controller.dto.response;

public record JwtToken(String grantType,String accessToken,String refreshToken) {
}
