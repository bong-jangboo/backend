package com.example.jangboo.transaction.controller.dto.request;

public record TransactionByDateRequest(
	String fromDate,
	String toDate
) {
}
