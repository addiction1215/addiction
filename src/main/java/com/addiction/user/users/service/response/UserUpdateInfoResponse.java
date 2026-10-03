package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateInfoResponse {

    @Schema(description = "수정된 휴대폰 번호", example = "010-1234-5678")
    private final String phoneNumber;
    @Schema(description = "수정된 이메일", example = "user@example.com")
    private final String email;

    @Builder
    public UserUpdateInfoResponse(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static UserUpdateInfoResponse createResponse(User user) {
        return UserUpdateInfoResponse.builder()
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
