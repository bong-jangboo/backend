package com.bongjangboo.legacy.transaction.controller.dto.response;

import com.bongjangboo.legacy.transaction.controller.dto.response.Info.TransactionInfo;

public record TransactionDetailResponse(
	TransactionInfo transactionInfo,
	String receiptUrl,
	Long receiptId
) {
}
