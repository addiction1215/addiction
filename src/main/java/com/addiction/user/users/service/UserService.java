package com.addiction.user.users.service;

import com.addiction.user.users.service.request.*;
import com.addiction.user.users.service.response.*;

public interface UserService {

	UserSaveResponse save(UserSaveServiceRequest userSaveServiceRequest);

	UserUpdateResponse update(UserUpdateServiceRequest userUpdateServiceRequest);

	UserUpdateSurveyResponse updateSurvey(UserUpdateSurveyServiceRequest userUpdateSurveyServiceRequest);

	UserUpdatePurposeResponse updatePurpose(UserUpdatePurposeServiceRequest userUpdatePurposeServiceRequest);

	UserUpdateProfileResponse updateProfile(UserUpdateProfileServiceRequest userUpdateProfileServiceRequest);

	UserUpdateInfoResponse updateInfo(UserUpdateInfoServiceRequest userUpdateInfoServiceRequest);
}
