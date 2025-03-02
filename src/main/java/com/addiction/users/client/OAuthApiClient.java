package com.addiction.users.client;

import com.addiction.users.entity.SnsType;

public interface OAuthApiClient {
    SnsType oAuthSnsType();
    String getEmail(String code);
}
