package com.example.legacy.transaction.controller.dto.response;

import java.util.List;

import com.example.legacy.transaction.controller.dto.response.Info.TransactionInfo;

public record TransactionPageResponse(
	List<TransactionInfo> transactions,
	int numberOfPages,
	int totalPages
) {
}
