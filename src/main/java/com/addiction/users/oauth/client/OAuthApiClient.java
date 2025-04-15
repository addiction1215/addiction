package com.addiction.users.oauth.client;

import com.addiction.users.entity.enums.SnsType;

public interface OAuthApiClient {
    SnsType oAuthSnsType();
    String getEmail(String code);
}
