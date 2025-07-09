package com.bongjangboo.legacy.receipt.service;
import com.bongjangboo.legacy.receipt.controller.dto.ocr.OcrRes;


public interface ReceiptOcrService {
    OcrRes.OcrResponse ocrStart(String imageUrl);
    OcrRes.OcrResponse getOcrData(String imageUrl);
}
