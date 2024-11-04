package com.example.jangboo.oauth.client.response.info;

public record MockTransactionInfo(
	String lable,
	String amount,
	String transactionType,
	String balance,
	String date,
	String time,
	String description
) {
}
