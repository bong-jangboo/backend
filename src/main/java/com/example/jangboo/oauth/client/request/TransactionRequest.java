package com.example.jangboo.oauth.client.request;

public record TransactionRequest(
	String bankTranId,
	String fintechUseNum,
	String fromDate,
	String toDate
) {
}
