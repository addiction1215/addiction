package com.addiction.user.users.service;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.response.*;

import java.util.List;

public interface UserReadService {

    List<User> findAll();

    List<User> findAllWithPushes();

	User findByEmail(String email);

	User findByEmailAndNickName(String email, String nickName);

	User findById(Long id);

	UserStartDateResponse findStartDate();

	UserPurposeResponse findPurpose();

	UserProfileResponse findProfile();

	UserInfoResponse findUserInfo();

	UserSimpleProfileResponse findSimpleProfile();
}
