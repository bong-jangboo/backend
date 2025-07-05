package com.example.legacy.receipt.service;
import com.example.legacy.receipt.controller.dto.ocr.OcrRes;


public interface ReceiptOcrService {
    OcrRes.OcrResponse ocrStart(String imageUrl);
    OcrRes.OcrResponse getOcrData(String imageUrl);
}
