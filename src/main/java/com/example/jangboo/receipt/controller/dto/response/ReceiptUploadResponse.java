package com.example.jangboo.receipt.controller.dto.response;

import java.util.List;

public record ReceiptUploadResponse(
        int successCount,
        int failureCount,
        List<ReceiptUploadResult> results
) {}
