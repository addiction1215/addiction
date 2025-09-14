package com.addiction.storage.controller;

import com.addiction.global.ApiResponse;
import com.addiction.storage.enums.BucketKind;
import com.addiction.storage.service.OracleStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storage")
public class StorageController {

    private final OracleStorageService oracleStorageService;

    @PostMapping("/{bucketKind}")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file, @PathVariable("bucketKind") BucketKind bucketKind) {
        return ApiResponse.ok(oracleStorageService.upload(file, bucketKind));
    }

}
