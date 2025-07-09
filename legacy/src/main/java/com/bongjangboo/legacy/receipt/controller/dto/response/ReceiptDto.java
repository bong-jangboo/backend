package com.bongjangboo.legacy.receipt.controller.dto.response;

import com.bongjangboo.legacy.receipt.domain.Receipt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReceiptDto {
    private Long receiptId;
    private String store;
    private int amount;
    private LocalDateTime transactionDate;

    public static ReceiptDto from(Receipt receipt) {
        return ReceiptDto.builder()
                .receiptId(receipt.getId())
                .store(receipt.getReceiptDetails().getStore())
                .amount(receipt.getReceiptDetails().getAmount())
                .transactionDate(receipt.getReceiptDetails().getTransactionDate())
                .build();

    }
}
