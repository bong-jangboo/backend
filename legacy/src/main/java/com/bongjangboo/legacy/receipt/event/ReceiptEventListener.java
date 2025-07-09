package com.bongjangboo.legacy.receipt.event;

import com.bongjangboo.legacy.file.event.ReceiptUploadedEvent;
import com.bongjangboo.legacy.global.error.CustomException;
import com.bongjangboo.legacy.receipt.controller.dto.ocr.OcrRes;
import com.bongjangboo.legacy.receipt.service.ReceiptService;
import com.bongjangboo.legacy.receipt.service.impl.ReceiptOcrServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceiptEventListener {

    private final ReceiptOcrServiceImpl receiptOcrService;
    private final ReceiptService receiptService;

    @EventListener
    @Async
    public void handleReceiptUploadedEvent(ReceiptUploadedEvent event){
        OcrRes.OcrResponse ocrResponse;
        try {
            ocrResponse = receiptOcrService.ocrStart(event.getFileUrl());
        } catch (CustomException e) {
            ocrResponse = OcrRes.OcrResponse.empty(); // Ocr 실패시 기본값
        }
        receiptService.saveOcrReceipt(event.getDeptId(), event.getFileUrl(), ocrResponse);

    }
}
