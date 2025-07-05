package com.example.legacy.file.controller.dto.response;

import com.example.legacy.file.domain.FileType;

public record FileUploadResult(
        FileType fileType,
        String fileName,
        boolean success,
        String fileUrl
) {
}
