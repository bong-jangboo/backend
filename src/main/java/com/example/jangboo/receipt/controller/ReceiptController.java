package com.example.jangboo.receipt.controller;

import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.global.dto.ResultDto;
import com.example.jangboo.receipt.controller.dto.response.ReceiptUploadResponse;
import com.example.jangboo.receipt.service.ReceiptService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResultDto<ReceiptUploadResponse>> upload(
            @RequestParam("receipts") List<MultipartFile> receipts,
            @AuthenticationPrincipal CurrentUserInfo userInfo) {
        ReceiptUploadResponse response = receiptService.uploadReceipt(receipts, userInfo);

        return ResponseEntity.ok(ResultDto.of(201,"영수증 업로드 결과",response));
    }
}
