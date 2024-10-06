package com.example.jangboo.receipt.controller.dto.response;

public record ReceiptUploadResult(
        String fileName,
        boolean success,
        String imgUrl,
        Long receiptId
) {}
