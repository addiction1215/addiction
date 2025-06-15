package com.addiction.user.userCigarette.service;

import com.addiction.user.userCigarette.service.request.ChangeType;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;

public interface UserCigaretteService {

	UserCigaretteFindResponse changeCigaretteCount(ChangeType changeType);
}
