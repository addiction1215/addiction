package com.addiction.users.dto.controller.request;

import com.addiction.users.dto.service.request.OAuthLoginServiceRequest;
import com.addiction.users.entity.enums.SnsType;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginOauthRequest {

    @NotNull(message = "Token은 필수입니다.")
    private String token;
    @NotNull(message = "인증타입은 필수입니다.")
    private SnsType snsType;
    @NotNull(message = "디바이스ID는 필수입니다.")
    private String deviceId;
    private String pushKey;

    @Builder
    private LoginOauthRequest(String token, SnsType snsType, String deviceId, String pushKey) {
        this.token = token;
        this.snsType = snsType;
        this.deviceId = deviceId;
        this.pushKey = pushKey;
    }

    public OAuthLoginServiceRequest toServiceRequest(){
        return OAuthLoginServiceRequest.builder()
            .token(token)
            .snsType(snsType)
            .deviceId(deviceId)
            .pushKey(pushKey)
            .build();
    }

}
