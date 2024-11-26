package com.example.jangboo.receipt.service.dto.response;

import java.time.LocalDateTime;

public record ReceiptInfoResponse(
	Long id,
	Integer amount,
	LocalDateTime transactionDate
) {
}
