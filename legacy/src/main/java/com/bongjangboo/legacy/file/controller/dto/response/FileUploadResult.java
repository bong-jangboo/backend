package com.bongjangboo.legacy.file.controller.dto.response;

import com.bongjangboo.legacy.file.domain.FileType;

public record FileUploadResult(
        FileType fileType,
        String fileName,
        boolean success,
        String fileUrl
) {
}
