package com.addiction.user.users.controller.request;

import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.service.request.UserUpdateProfileServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateProfileRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickName;
    private String introduction;
    private Sex sex;
    private String birthDay;
    private String profileUrl;
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
