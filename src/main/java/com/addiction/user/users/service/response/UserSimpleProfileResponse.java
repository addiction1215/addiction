package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SnsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSimpleProfileResponse {

    @Schema(description = "프로필 이미지 URL. 기본 이미지면 null입니다.", example = "https://example.com/profile.jpg", nullable = true)
    private final String profileUrl;
    @Schema(description = "사용자 이메일", example = "user@example.com")
    private final String email;
    @Schema(description = "사용자 닉네임", example = "금연메이트")
    private final String nickName;
    @Schema(description = "가입 또는 로그인 제공자", example = "KAKAO", allowableValues = {"NORMAL", "KAKAO", "GOOGLE", "NAVER"})
    private final SnsType snsType;

    @Builder
    public UserSimpleProfileResponse(String profileUrl, String email, String nickName, SnsType snsType) {
        this.profileUrl = profileUrl;
        this.email = email;
        this.nickName = nickName;
        this.snsType = snsType;
    }

    public static UserSimpleProfileResponse createResponse(User user, String profileUrl) {
        return UserSimpleProfileResponse.builder()
                .profileUrl(profileUrl)
                .email(user.getEmail())
                .nickName(user.getNickName())
                .snsType(user.getSnsType())
                .build();
    }
}
