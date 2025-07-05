package com.example.legacy.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String directory) throws IOException;
    void deleteFile(String fileUrl);
}
