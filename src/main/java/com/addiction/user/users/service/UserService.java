package com.addiction.user.users.service;

import com.addiction.user.users.service.request.UserSaveServiceRequest;
import com.addiction.user.users.service.request.UserUpdatePurposeServiceRequest;
import com.addiction.user.users.service.request.UserUpdateServiceRequest;
import com.addiction.user.users.service.request.UserUpdateSurveyServiceRequest;
import com.addiction.user.users.service.response.UserSaveResponse;
import com.addiction.user.users.service.response.UserStartDateResponse;
import com.addiction.user.users.service.response.UserUpdatePurposeResponse;
import com.addiction.user.users.service.response.UserUpdateResponse;
import com.addiction.user.users.service.response.UserUpdateSurveyResponse;

public interface UserService {

	UserSaveResponse save(UserSaveServiceRequest userSaveServiceRequest);

	UserUpdateResponse update(UserUpdateServiceRequest userUpdateServiceRequest);

	UserUpdateSurveyResponse updateSurvey(UserUpdateSurveyServiceRequest userUpdateSurveyServiceRequest);

	UserUpdatePurposeResponse updatePurpose(UserUpdatePurposeServiceRequest userUpdatePurposeServiceRequest);
}
