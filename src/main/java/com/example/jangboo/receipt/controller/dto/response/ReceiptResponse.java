package com.example.jangboo.receipt.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReceiptResponse {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private int totalReceipts;
    private List<ReceiptDto> receipts;
}
