package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Sex;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateProfileResponse {

    private final String nickName;
    private final String introduction;
    private final Sex sex;
    private final String birthDay;

    @Builder
    public UserUpdateProfileResponse(String birthDay, String introduction, String nickName, Sex sex) {
        this.birthDay = birthDay;
        this.introduction = introduction;
        this.nickName = nickName;
        this.sex = sex;
    }

    public static UserUpdateProfileResponse createResponse(User user) {
        return UserUpdateProfileResponse.builder()
                .birthDay(user.getBirthDay())
                .introduction(user.getIntroduction())
                .nickName(user.getNickName())
                .sex(user.getSex())
                .build();
    }

}
