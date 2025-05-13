package com.addiction.user.users.service;

import com.addiction.user.users.service.request.OAuthLoginServiceRequest;
import com.addiction.user.users.service.response.OAuthLoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface LoginService {

	OAuthLoginResponse oauthLogin(OAuthLoginServiceRequest oAuthLoginServiceRequest) throws
		JsonProcessingException;

}
