package com.addiction.user.users.controller.request;

import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.service.request.UserUpdateProfileServiceRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateProfileRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    @Schema(description = "변경할 닉네임", example = "금연메이트")
    private String nickName;
    @Schema(description = "프로필 소개", example = "하루 한 걸음씩 금연 중이에요.")
    private String introduction;
    @Schema(description = "사용자 성별", example = "MALE", allowableValues = {"FEMALE", "MALE"})
    private Sex sex;
    @Schema(description = "생년월일 8자리 문자열", example = "19960101")
    private String birthDay;
    @Schema(description = "변경할 프로필 이미지 URL. resetProfileImage가 true이면 함께 보낼 수 없습니다.", example = "https://example.com/profile.jpg")
    private String profileUrl;
    @Schema(description = "true이면 프로필 이미지를 기본 이미지로 초기화합니다. profileUrl과 동시 전송할 수 없습니다.", example = "false")
    private Boolean resetProfileImage;

    @Builder
    public UserUpdateProfileRequest(String profileUrl, String birthDay, String introduction, String nickName, Sex sex,
                                    Boolean resetProfileImage) {
        this.profileUrl = profileUrl;
        this.birthDay = birthDay;
        this.introduction = introduction;
        this.nickName = nickName;
        this.sex = sex;
        this.resetProfileImage = resetProfileImage;
    }

    public UserUpdateProfileServiceRequest toServiceRequest() {
        return UserUpdateProfileServiceRequest.builder()
                .profileUrl(profileUrl)
                .birthDay(birthDay)
                .introduction(introduction)
                .nickName(nickName)
                .sex(sex)
                .resetProfileImage(resetProfileImage)
                .build();
    }
}
