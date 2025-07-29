package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Sex;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponse {

    private final String nickName;
    private final String introduction;
    private final Sex sex;
    private final String birthDay;

    @Builder
    public UserProfileResponse(String birthDay, String nickName, String introduction, Sex sex) {
        this.birthDay = birthDay;
        this.nickName = nickName;
        this.introduction = introduction;
        this.sex = sex;
    }

    public static UserProfileResponse createResponse(User user) {
        return UserProfileResponse.builder()
                .birthDay(user.getBirthDay())
                .nickName(user.getNickName())
                .introduction(user.getIntroduction())
                .sex(user.getSex())
                .build();
    }
}
