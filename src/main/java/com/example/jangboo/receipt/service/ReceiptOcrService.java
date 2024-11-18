package com.example.jangboo.receipt.service;
import com.example.jangboo.receipt.controller.dto.ocr.OcrRes;


public interface ReceiptOcrService {
    OcrRes.OcrResponse ocrStart(String imageUrl);
    OcrRes.OcrResponse getOcrData(String imageUrl);
}
