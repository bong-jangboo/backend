package com.example.legacy.oauth.client.request;

public record TransactionRequest(
	String bankTranId,
	String fintechUseNum,
	String fromDate,
	String toDate,
	String fromTime,
	String toTime
) {
}
