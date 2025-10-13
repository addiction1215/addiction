package com.addiction.storage.service;

import com.addiction.storage.enums.Bucket;
import com.addiction.storage.enums.BucketKind;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OracleStorageService {

    private final ObjectStorage objectStorageClient;
    private final Bucket bucket;

    @Value("${oracle.name-space-name}")
    private String nameSpaceName;

    @Value("${oracle.cloud.region}")
    private String region;


    /**
     * 사용자 프로필 이미지 업로드 (특화 메서드) - 영구 URL 버전
     *
     * @param file   프로필 이미지 파일
     * @return 업로드된 파일의 URL
     */
    public String upload(MultipartFile file, BucketKind bucketKind) {
        validateImageFile(file);
        String bucketName = bucket.getBucketName(bucketKind);

        String objectName = uploadFile(file, nameSpaceName, bucketName);

        return createPermanentUrl(objectName, nameSpaceName, bucketName);
    }


    /**
     * MultipartFile을 Oracle Object Storage에 업로드
     *
     * @param file           업로드할 파일
     * @return 업로드된 객체명
     */
    public String uploadFile(MultipartFile file, String nameSpaceName, String bucketName) {
        validateFile(file);

        try {
            String objectName = generateObjectName(file.getOriginalFilename());
            String contentType = determineContentType(file);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .namespaceName(nameSpaceName)
                    .bucketName(bucketName)
                    .objectName(objectName)
                    .contentType(contentType)
                    .contentLength(file.getSize())
                    .putObjectBody(file.getInputStream())
                    .build();

            PutObjectResponse response = objectStorageClient.putObject(putObjectRequest);

            log.info("파일 업로드 성공: {}, ETag: {}", objectName, response.getETag());
            return objectName;

        } catch (IOException e) {
            log.error("파일 업로드 중 IO 오류 발생: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("파일 업로드 실패", e);
        } catch (Exception e) {
            log.error("파일 업로드 중 오류 발생: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    /**
     * 영구적인 공개 URL 생성 (버킷이 public이어야 함)
     *
     * @param objectName 객체명
     * @return 영구 접근 가능한 URL
     */
    public String createPermanentUrl(String objectName, String namespaceName, String bucketName) {
        // Public Bucket을 위한 직접 URL
        String permanentUrl = String.format("https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                region, namespaceName, bucketName, objectName);

        log.info("영구 URL 생성: {}", objectName);
        return permanentUrl;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }
    }

    private void validateImageFile(MultipartFile file) {
        validateFile(file);

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }

        String[] allowedTypes = {"image/jpeg", "image/png", "image/gif", "image/webp"};
        boolean isAllowed = false;
        for (String allowedType : allowedTypes) {
            if (allowedType.equals(contentType)) {
                isAllowed = true;
                break;
            }
        }

        if (!isAllowed) {
            throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다. (JPEG, PNG, GIF, WebP만 지원)");
        }
    }

    private String generateObjectName(String originalFilename) {
        String filename = UUID.randomUUID().toString();

        // 파일 확장자 처리
        String extension = getFileExtension(originalFilename);
        if (!filename.endsWith(extension)) {
            filename += extension;
        }

        return filename;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }

        return filename.substring(lastDotIndex).toLowerCase();
    }

    private String determineContentType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null && !contentType.isEmpty()) {
            return contentType;
        }

        // 파일 확장자로 추정
        String extension = getFileExtension(file.getOriginalFilename());
        return switch (extension) {
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".webp" -> "image/webp";
            case ".pdf" -> "application/pdf";
            case ".txt" -> "text/plain";
            case ".json" -> "application/json";
            default -> "application/octet-stream";
        };
    }
}