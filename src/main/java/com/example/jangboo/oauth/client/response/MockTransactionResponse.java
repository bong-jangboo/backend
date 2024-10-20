package com.example.jangboo.oauth.client.response;

import java.util.List;

import com.example.jangboo.oauth.client.response.info.MockTransactionInfo;

public record MockTransactionResponse(
	String fintechUseNum,
	String startDate,
	String toDate,
	List<MockTransactionInfo> transactions

) {
}

