package com.addiction.user.users.service;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.response.*;

public interface UserReadService {

	User findByEmail(String email);

	User findByEmailAndNickName(String email, String nickName);

	User findById(Long id);

	UserStartDateResponse findStartDate();

	UserPurposeResponse findPurpose();

	UserProfileResponse findProfile();

	UserInfoResponse findUserInfo();

	UserSimpleProfileResponse findSimpleProfile();
}
