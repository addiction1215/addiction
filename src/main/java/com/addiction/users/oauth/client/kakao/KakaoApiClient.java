package com.addiction.users.oauth.client.kakao;

import org.springframework.stereotype.Component;

import com.addiction.users.oauth.client.OAuthApiClient;
import com.addiction.users.entity.SnsType;
import com.addiction.users.oauth.feign.kakao.KakaoApiFeignCall;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    private final KakaoApiFeignCall kakaoApiFeignCall;

    @Override
    public SnsType oAuthSnsType() {
        return SnsType.KAKAO;
    }

    @Override
    public String getEmail(String accessToken){
        return kakaoApiFeignCall.getUserInfo("Bearer " + accessToken).getEmail();
    }
}
