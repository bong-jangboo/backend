package com.example.jangboo.receipt.event;

import com.example.jangboo.file.event.ReceiptUploadedEvent;
import com.example.jangboo.receipt.controller.dto.ocr.OcrRes;
import com.example.jangboo.receipt.domain.ReceiptRepository;
import com.example.jangboo.receipt.service.ReceiptService;
import com.example.jangboo.receipt.service.impl.ReceiptOcrServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class ReceiptEventListener {

    private final ReceiptOcrServiceImpl receiptOcrService;
    private final ReceiptService receiptService;

    @EventListener
    @Async
    public void handleReceiptUploadedEvent(ReceiptUploadedEvent event){
        OcrRes.OcrResponse ocrResponse = receiptOcrService.ocrStart(event.getFileUrl());
        receiptService.saveOcrReceipt(event.getDeptId(), event.getFileUrl(), ocrResponse);
    }
}
