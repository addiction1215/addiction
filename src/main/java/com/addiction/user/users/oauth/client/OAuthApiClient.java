package com.addiction.user.users.oauth.client;

import com.addiction.user.users.entity.enums.SnsType;

public interface OAuthApiClient {
    SnsType oAuthSnsType();
    String getEmail(String code);
}
