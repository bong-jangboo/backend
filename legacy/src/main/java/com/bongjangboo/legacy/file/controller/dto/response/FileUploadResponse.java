package com.bongjangboo.legacy.file.controller.dto.response;

import java.util.List;

public record FileUploadResponse(
        int successCount,
        int failureCount,
        List<FileUploadResult> fileUploadResults

) {
}
