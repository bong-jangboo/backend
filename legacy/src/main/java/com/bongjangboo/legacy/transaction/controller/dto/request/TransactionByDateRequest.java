package com.bongjangboo.legacy.transaction.controller.dto.request;

public record TransactionByDateRequest(
	String fromDate,
	String toDate
) {
}
