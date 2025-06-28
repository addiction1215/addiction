package com.addiction.user.userCigarette.service;

import java.util.List;

import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;

public interface UserCigaretteReadService {
	UserCigaretteFindResponse findUserCigaretteCount();
	List<UserCigarette> findAll();
}
