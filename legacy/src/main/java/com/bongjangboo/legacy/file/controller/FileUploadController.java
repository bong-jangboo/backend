package com.bongjangboo.legacy.file.controller;

import com.bongjangboo.legacy.auth.controller.dto.Info.CurrentUserInfo;
import com.bongjangboo.legacy.file.controller.dto.response.FileUploadResponse;
import com.bongjangboo.legacy.file.service.ReceiptUploadService;
import com.bongjangboo.legacy.global.dto.ResultDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final ReceiptUploadService receiptUploadService;

    public FileUploadController(ReceiptUploadService receiptUploadService) {
        this.receiptUploadService = receiptUploadService;
    }

    @PostMapping("/receipt")
    public ResponseEntity<ResultDto<FileUploadResponse>> receiptUpload(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("documentId") Optional<Long> documentId,
            @AuthenticationPrincipal CurrentUserInfo userInfo) {

        FileUploadResponse response = receiptUploadService.uploadReceipt(files, userInfo, documentId);

        return ResponseEntity.ok(ResultDto.of(201, "파일 업로드 결과", response));
    }
}
