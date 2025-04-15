package com.addiction.user.users.oauth.client.naver;

import org.springframework.stereotype.Component;

import com.addiction.user.users.oauth.client.OAuthApiClient;
import com.addiction.user.users.entity.enums.SnsType;
import com.addiction.user.users.oauth.feign.naver.NaverApiFeignCall;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NaverApiClient implements OAuthApiClient {

    private final NaverApiFeignCall naverApiFeignCall;

    @Override
    public SnsType oAuthSnsType() {
        return SnsType.NAVER;
    }

    @Override
    public String getEmail(String accessToken){
        return naverApiFeignCall.getUserInfo("Bearer " + accessToken).getEmail();
    }
}
