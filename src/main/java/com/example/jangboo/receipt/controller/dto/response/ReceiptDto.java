package com.example.jangboo.receipt.controller.dto.response;

import com.example.jangboo.receipt.domain.Receipt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReceiptDto {
    private Long receiptId;
    private String store;
    private int amount;
    private String transactionDate;

    public static ReceiptDto from(Receipt receipt) {
        ReceiptDto dto = new ReceiptDto().builder()
                .receiptId(receipt.getId())
                .store(receipt.getReceiptDetails().getStore())
                .amount(receipt.getReceiptDetails().getAmount())
                .transactionDate(receipt.getReceiptDetails().getTransactionDate())
                .build();

    }
}
