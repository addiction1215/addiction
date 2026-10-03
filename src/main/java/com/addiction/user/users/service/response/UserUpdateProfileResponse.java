package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateProfileResponse {

    @Schema(description = "수정된 닉네임", example = "금연메이트")
    private final String nickName;
    @Schema(description = "프로필 소개", example = "하루 한 걸음씩 금연 중이에요.")
    private final String introduction;
    @Schema(description = "사용자 성별", example = "MALE", allowableValues = {"FEMALE", "MALE"})
    private final Sex sex;
    @Schema(description = "생년월일 8자리 문자열", example = "19960101")
    private final String birthDay;
    @Schema(description = "프로필 이미지 URL. 기본 이미지면 null입니다.", example = "https://example.com/profile.jpg", nullable = true)
    private final String profileUrl;

    @Builder
    public UserUpdateProfileResponse(String profileUrl, String birthDay, String introduction, String nickName, Sex sex) {
        this.profileUrl = profileUrl;
        this.birthDay = birthDay;
        this.introduction = introduction;
        this.nickName = nickName;
        this.sex = sex;
    }

    public static UserUpdateProfileResponse createResponse(User user) {
        return UserUpdateProfileResponse.builder()
                .birthDay(user.getBirthDay())
                .profileUrl(user.getProfileUrl())
                .introduction(user.getIntroduction())
                .nickName(user.getNickName())
                .sex(user.getSex())
                .build();
    }

}
