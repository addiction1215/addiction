package com.addiction.user.userCigarette.service;

import java.util.List;

import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;

public interface UserCigaretteReadService {
	UserCigarette findByUserId(int userId);
	UserCigaretteFindResponse findUserCigaretteCount();
	List<UserCigarette> findAll();
}
