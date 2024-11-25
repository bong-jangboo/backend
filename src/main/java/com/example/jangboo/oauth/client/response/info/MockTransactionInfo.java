package com.example.jangboo.oauth.client.response.info;

public record MockTransactionInfo(
	String label,
	String amount,
	String transaction_type,
	String balance,
	String date,
	String time,
	String description
) {
}
