package com.bongjangboo.legacy.oauth.client.response;

import java.util.List;

import com.bongjangboo.legacy.oauth.client.response.info.MockTransactionInfo;

public record MockTransactionResponse(
	String fintechUseNum,
	String startDate,
	String endDate,
	String startTime,
	String endTime,
	List<MockTransactionInfo> transactions
) {
}

