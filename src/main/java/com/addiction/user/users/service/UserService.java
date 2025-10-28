package com.addiction.user.users.service;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.request.*;
import com.addiction.user.users.service.response.*;

public interface UserService {

	User save(User user);

	UserSaveResponse save(UserSaveServiceRequest userSaveServiceRequest);

	UserUpdateResponse update(UserUpdateServiceRequest userUpdateServiceRequest);

	UserUpdateSurveyResponse updateSurvey(UserUpdateSurveyServiceRequest userUpdateSurveyServiceRequest);

	UserUpdatePurposeResponse updatePurpose(UserUpdatePurposeServiceRequest userUpdatePurposeServiceRequest);

	UserUpdateProfileResponse updateProfile(UserUpdateProfileServiceRequest userUpdateProfileServiceRequest); // 테스트코드X

	UserUpdateInfoResponse updateInfo(UserUpdateInfoServiceRequest userUpdateInfoServiceRequest); // 테스트코드X

    Boolean withdraw();
}
