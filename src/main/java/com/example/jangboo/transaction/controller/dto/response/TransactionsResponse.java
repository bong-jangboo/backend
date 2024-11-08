package com.example.jangboo.transaction.controller.dto.response;

import java.util.List;

import com.example.jangboo.transaction.controller.dto.response.Info.TransactionInfo;

public record TransactionsResponse(
	List<TransactionInfo> transactions
) {
}
