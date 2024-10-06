package com.example.jangboo.receipt.service;

import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.receipt.controller.dto.response.ReceiptUploadResponse;
import com.example.jangboo.receipt.controller.dto.response.ReceiptUploadResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiptService {

    public ReceiptUploadResponse uploadReceipt(List<MultipartFile> images, CurrentUserInfo userInfo) {
        int successCount = 0;
        int failureCount = 0;
        List<ReceiptUploadResult> results = new ArrayList<>();

        for (MultipartFile image : images) {
            String filename = image.getOriginalFilename();
            try {
                // 파일 업로드 로직
                // s3 이용

                // 영수증 정보 저장 로직
                // 외부 api 이용
                // 비동기 처리
                results.add(new ReceiptUploadResult(filename, true, "test", 1L));
                successCount++;
            } catch (Exception e){
                failureCount++;
            }
        }
        return new ReceiptUploadResponse(successCount, failureCount, results);
    }

}
