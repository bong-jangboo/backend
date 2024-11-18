package com.example.jangboo.users.controller.dto.response;

import lombok.Builder;

@Builder
public record UserInfoResponse(
	String name,
	String parent,
	String dept,
	String role,
	String number
) {
}
