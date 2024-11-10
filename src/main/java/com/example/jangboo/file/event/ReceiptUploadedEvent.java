package com.example.jangboo.file.event;

import lombok.Getter;

@Getter
public class ReceiptUploadedEvent {
    private final Long fileId;
    private final String fileUrl;

    public ReceiptUploadedEvent(Long fileId, String fileUrl) {
        this.fileId = fileId;
        this.fileUrl = fileUrl;
    }
}
