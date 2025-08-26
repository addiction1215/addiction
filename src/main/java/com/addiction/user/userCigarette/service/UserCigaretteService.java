package com.addiction.user.userCigarette.service;

import com.addiction.user.userCigarette.service.request.UserCigaretteChangeServiceRequest;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;

public interface UserCigaretteService {

	Long changeCigarette(UserCigaretteChangeServiceRequest userCigaretteChangeServiceRequest);

}
