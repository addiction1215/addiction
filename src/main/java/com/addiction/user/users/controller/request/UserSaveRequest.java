package com.addiction.user.users.controller.request;

import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.service.request.UserSaveServiceRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequest {

	@NotNull(message = "이메일은 필수입니다.")
	private String email;
	@NotNull(message = "비밀번호는 필수입니다.")
	private String password;
	@NotNull(message = "닉네임은 필수입니다.")
	private String nickName;
    @NotNull(message = "성별은 필수입니다.")
    private Sex sex;
    @NotNull(message = "생년월일은 필수입니다.")
    private String birthDay;

    @Builder
    public UserSaveRequest(String birthDay, String email, String nickName, String password, Sex sex) {
        this.birthDay = birthDay;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.sex = sex;
    }

    public UserSaveServiceRequest toServiceRequest() {
        return UserSaveServiceRequest.builder()
                .birthDay(birthDay)
                .email(email)
                .nickName(nickName)
                .password(password)
                .sex(sex)
                .build();
	}
}
