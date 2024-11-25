package com.example.jangboo.users.controller.dto.response;

import com.example.jangboo.transaction.controller.dto.response.Info.TransactionInfo;
import com.example.jangboo.transaction.domain.Transaction;
import com.example.jangboo.users.domain.User;

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
