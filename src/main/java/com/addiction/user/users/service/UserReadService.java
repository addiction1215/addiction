package com.addiction.user.users.service;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.response.UserInfoResponse;
import com.addiction.user.users.service.response.UserProfileResponse;
import com.addiction.user.users.service.response.UserPurposeResponse;
import com.addiction.user.users.service.response.UserStartDateResponse;

public interface UserReadService {

	User findByEmail(String email);

	User findById(int id);

	UserStartDateResponse findStartDate();

	UserPurposeResponse findPurpose();

	UserProfileResponse findProfile();

	UserInfoResponse findUserInfo();
}
