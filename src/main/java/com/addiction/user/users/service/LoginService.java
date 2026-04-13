package com.addiction.user.users.service;

import com.addiction.user.users.service.request.LoginServiceRequest;
import com.addiction.user.users.service.request.OAuthLoginServiceRequest;
import com.addiction.user.users.service.request.FindPasswordServiceRequest;
import com.addiction.user.users.service.request.SendAuthCodeServiceRequest;
import com.addiction.user.users.service.request.VerifyAuthCodeServiceRequest;
import com.addiction.user.users.service.response.FindPasswordResponse;
import com.addiction.user.users.service.response.LoginResponse;
import com.addiction.user.users.service.response.OAuthLoginResponse;
import com.addiction.user.users.service.response.SendAuthCodeResponse;
import com.addiction.user.users.service.response.VerifyAuthCodeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface LoginService {

	LoginResponse normalLogin(LoginServiceRequest loginServiceRequest) throws JsonProcessingException;

	OAuthLoginResponse oauthLogin(OAuthLoginServiceRequest oAuthLoginServiceRequest) throws
		JsonProcessingException;

    SendAuthCodeResponse sendMail(SendAuthCodeServiceRequest sendAuthCodeServiceRequest);

    VerifyAuthCodeResponse verifyAuthCode(VerifyAuthCodeServiceRequest verifyAuthCodeServiceRequest);

    FindPasswordResponse findPassword(FindPasswordServiceRequest findPasswordServiceRequest);
}
