package com.addiction.user.users.controller.request;

import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.service.request.UserUpdateProfileServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateProfileRequest {

    @NotNull(message = "닉네임은 필수입니다.")
    private String nickName;
    private String introduction;
    @NotNull(message = "성별은 필수입니다.")
    private Sex sex;
    @NotNull(message = "생년월일은 필수입니다.")
    private String birthDay;

    @Builder
    public UserUpdateProfileRequest(String birthDay, String introduction, String nickName, Sex sex) {
        this.birthDay = birthDay;
        this.introduction = introduction;
        this.nickName = nickName;
        this.sex = sex;
    }

    public UserUpdateProfileServiceRequest toServiceRequest() {
        return UserUpdateProfileServiceRequest.builder()
                .birthDay(birthDay)
                .introduction(introduction)
                .nickName(nickName)
                .sex(sex)
                .build();
    }
}
