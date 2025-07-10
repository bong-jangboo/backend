package com.bongjangboo.legacy.file.event;

import lombok.Getter;

@Getter
public class ReceiptUploadedEvent {
    private final Long deptId;
    private final Long fileId;
    private final String fileUrl;

    public ReceiptUploadedEvent(Long deptId, Long fileId, String fileUrl) {
        this.deptId = deptId;
        this.fileId = fileId;
        this.fileUrl = fileUrl;
    }
}
