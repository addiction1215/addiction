package com.addiction.storage.enums;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Bucket {

    @Value("${oracle.user.bucket-name}")
    private String userBucket;

    @Value("${oracle.challenge.bucket-name}")
    private String challengeBucket;

    public String getBucketName(BucketKind bucketKind) {
        return switch (bucketKind) {
            case USER -> userBucket;
            case CHALLENGE -> challengeBucket;
        };
    }
}