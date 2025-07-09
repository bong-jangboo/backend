package com.bongjangboo.legacy.auth.controller.dto.request;

public record LoginRequest(
	String loginId,
	String password
) {
}
