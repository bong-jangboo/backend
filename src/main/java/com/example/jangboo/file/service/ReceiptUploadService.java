package com.example.jangboo.file.service;

import com.example.jangboo.auth.controller.dto.Info.CurrentUserInfo;
import com.example.jangboo.file.controller.dto.response.FileUploadResponse;
import com.example.jangboo.file.controller.dto.response.FileUploadResult;
import com.example.jangboo.file.domain.File;
import com.example.jangboo.file.domain.FileRepository;
import com.example.jangboo.file.domain.FileStatus;
import com.example.jangboo.file.domain.FileType;
import com.example.jangboo.file.event.ReceiptUploadedEvent;
import com.example.jangboo.infra.aws.S3FileStorageService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptUploadService {
    private final FileStorageService fileStorageService;
    private final FileRepository fileRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReceiptUploadService(FileRepository fileRepository, FileStorageService fileStorageService, ApplicationEventPublisher eventPublisher) {
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public FileUploadResponse uploadReceipt(List<MultipartFile> files, CurrentUserInfo userInfo, Optional<Long> documentId) {
        List<FileUploadResult> fileUploadResults = new ArrayList<>();


        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                fileName = "unknown";
            }

            try {
                validateFile(file, FileType.RECEIPT);
                // s3에 파일 업로드
                String fileUrl = fileStorageService.uploadFile(file,"receipt");

                // 파일정보 저장
                File savedfile = fileRepository.save(new File(fileName, userInfo.deptId(),fileUrl,FileType.RECEIPT, FileStatus.UPLOADED));
                eventPublisher.publishEvent(new ReceiptUploadedEvent(userInfo.deptId(), savedfile.getId(), savedfile.getFileUrl()));
                // 업로드 결과 추가
                fileUploadResults.add(new FileUploadResult(FileType.RECEIPT, fileName,true, fileUrl));

            } catch (Exception e){
                fileUploadResults.add(new FileUploadResult(FileType.RECEIPT, fileName,false, null));

            }
        }

        // 성공 및 실패 횟수 계산
        int successCount = (int) fileUploadResults.stream().filter(FileUploadResult::success).count();
        int failedCount = fileUploadResults.size() - successCount;

        return new FileUploadResponse(successCount, failedCount, fileUploadResults);
    }


    private void validateFile(MultipartFile file, FileType fileType) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (fileType == FileType.RECEIPT) {
            // 파일 확장자 확인 (예: 이미지 파일만 허용)
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && !originalFilename.matches(".*\\.(jpg|jpeg|png|pdf)$")) {
                throw new IllegalArgumentException("Invalid file type. Only JPG, JPEG, PNG, and PDF are allowed.");
            }
        }
    }
}
