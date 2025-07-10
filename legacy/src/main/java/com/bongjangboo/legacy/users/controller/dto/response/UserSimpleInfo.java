package com.bongjangboo.legacy.users.controller.dto.response;

import com.bongjangboo.legacy.users.domain.User;

import lombok.Builder;

@Builder
public record UserSimpleInfo(
	String name,
	String number,
	Boolean isPayed
) {
	public static UserSimpleInfo from(User user) {
		return UserSimpleInfo.builder()
			.name(user.getName())
			.number(user.getNumber())
			.isPayed(user.getIsPayed())
			.build();
	}
}
