package com.addiction.user.users.service;

import com.addiction.user.users.service.request.LoginServiceRequest;
import com.addiction.user.users.service.request.OAuthLoginServiceRequest;
import com.addiction.user.users.service.response.LoginResponse;
import com.addiction.user.users.service.response.OAuthLoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface LoginService {

	LoginResponse normalLogin(LoginServiceRequest loginServiceRequest) throws JsonProcessingException;

	OAuthLoginResponse oauthLogin(OAuthLoginServiceRequest oAuthLoginServiceRequest) throws
		JsonProcessingException;


}
