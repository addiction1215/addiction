package com.addiction.user.users.service.response;

import com.addiction.user.users.entity.User;

import com.addiction.user.users.entity.enums.Sex;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserSaveResponse {

    private final String email;
    private final String nickName;
    private final Sex sex;
    private final String birthDay;

    @Builder
    public UserSaveResponse(String birthDay, String email, String nickName, Sex sex) {
        this.birthDay = birthDay;
        this.email = email;
        this.nickName = nickName;
        this.sex = sex;
    }

    public static UserSaveResponse createResponse(User user) {
        return UserSaveResponse.builder()
                .birthDay(user.getBirthDay())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .sex(user.getSex())
                .build();
    }
}
