package com.example.legacy.users.controller.dto.response;

import java.util.List;

public record UserListResponse(
	List<UserSimpleInfo> users,
	int numberOfPages,
	int totalPages
) {
}
