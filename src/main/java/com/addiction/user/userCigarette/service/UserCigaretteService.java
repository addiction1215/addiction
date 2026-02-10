package com.addiction.user.userCigarette.service;

import java.util.List;

import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.request.UserCigaretteChangeServiceRequest;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;

public interface UserCigaretteService {

	Long changeCigarette(UserCigaretteChangeServiceRequest userCigaretteChangeServiceRequest);

	void deleteAll(List<UserCigarette> userCigarettes);

}
