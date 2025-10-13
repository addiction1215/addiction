package com.addiction.user.users.service.request;

import com.addiction.user.users.entity.enums.Sex;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateProfileServiceRequest {

    private final String nickName;
    private final String introduction;
    private final Sex sex;
    private final String birthDay;
    private final String profileUrl;

    @Builder
    public UserUpdateProfileServiceRequest(String profileUrl, String birthDay, String introduction, String nickName, Sex sex) {
        this.birthDay = birthDay;
        this.introduction = introduction;
        this.nickName = nickName;
        this.sex = sex;
        this.profileUrl = profileUrl;
    }

}
