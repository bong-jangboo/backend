package com.example.legacy.infra.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.legacy.file.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3FileStorageService implements FileStorageService {

    private final AmazonS3Client amazonS3Client;
    public S3FileStorageService(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucket;

    @Override
    public String uploadFile(MultipartFile multipartFile, String directoryPath) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString().substring(0,8) + "_" + originalFilename;

        // 디렉토리 경로를 포함한 파일 키 생성
        String key = directoryPath + "/" + uniqueFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(bucket, key, multipartFile.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, key).toString();
    }

    @Override
    public void deleteFile(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        amazonS3Client.deleteObject(bucket, key);
    }

    private String extractKeyFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.indexOf(bucket) + bucket.length() + 1);
    }


}
