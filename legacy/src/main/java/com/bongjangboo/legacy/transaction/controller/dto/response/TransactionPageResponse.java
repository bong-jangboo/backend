package com.bongjangboo.legacy.transaction.controller.dto.response;

import java.util.List;

import com.bongjangboo.legacy.transaction.controller.dto.response.Info.TransactionInfo;

public record TransactionPageResponse(
	List<TransactionInfo> transactions,
	int numberOfPages,
	int totalPages
) {
}
