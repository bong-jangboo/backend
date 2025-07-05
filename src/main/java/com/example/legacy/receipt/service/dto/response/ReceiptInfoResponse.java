package com.example.legacy.receipt.service.dto.response;

import java.time.LocalDateTime;

public record ReceiptInfoResponse(
	Long id,
	Integer amount,
	LocalDateTime transactionDate
) {
}
