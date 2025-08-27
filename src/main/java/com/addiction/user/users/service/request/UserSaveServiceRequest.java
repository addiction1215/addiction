package com.addiction.user.users.service.request;

import com.addiction.user.users.entity.enums.Sex;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.Role;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserSaveServiceRequest {

    private final String email;
    private final String password;
    private final String nickName;
    private final Sex sex;
    private final String birthDay;

    @Builder
    public UserSaveServiceRequest(String birthDay, String email, String password, String nickName, Sex sex) {
        this.birthDay = birthDay;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.sex = sex;
    }

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .birthDay(birthDay)
                .snsType(SnsType.NORMAL)
                .role(Role.USER)
                .settingStatus(SettingStatus.INCOMPLETE)
                .nickName(nickName)
                .sex(sex)
                .build();
    }
}
