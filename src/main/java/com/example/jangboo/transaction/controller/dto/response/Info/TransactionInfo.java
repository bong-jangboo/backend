package com.example.jangboo.transaction.controller.dto.response.Info;

import lombok.Builder;

@Builder
public record TransactionInfo(
	String lable,
	String amount,
	String transaction_type,
	String balance,
	String date,
	String time,
	String description
) {
}
