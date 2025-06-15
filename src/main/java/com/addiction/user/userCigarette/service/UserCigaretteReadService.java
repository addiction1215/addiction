package com.addiction.user.userCigarette.service;

import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;

public interface UserCigaretteReadService {
	UserCigarette findByUserId(int userId);
	UserCigaretteFindResponse findUserCigaretteCount();
}
