package com.addiction.users.client.google;

import org.springframework.stereotype.Component;

import com.addiction.users.client.OAuthApiClient;
import com.addiction.users.entity.SnsType;
import com.addiction.users.feign.google.GoogleApiFeignCall;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleApiClient implements OAuthApiClient {

    private final GoogleApiFeignCall googleApiFeignCall;

    @Override
    public SnsType oAuthSnsType() {
        return SnsType.GOOGLE;
    }

    @Override
    public String getEmail(String accessToken){
        return googleApiFeignCall.getUserInfo("Bearer " + accessToken).getEmail();
    }
}
