package com.example.jangboo.users.controller.dto.response;

import lombok.Builder;

@Builder
public record UserInfoResponse(
	String name,
	String dept,
	String parent,
	String role,
	String number,
	Boolean isPayed
) {
}
