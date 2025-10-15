package com.addiction.user.userCigarette.service;

import java.time.LocalDateTime;
import java.util.List;

import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.response.UserCigaretteFindResponse;

public interface UserCigaretteReadService {
	UserCigaretteFindResponse findUserCigaretteCount();
	List<UserCigarette> findAll();
	List<UserCigarette> findAllByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
	List<UserCigarette> findAllByUserIdAndCreatedDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
	UserCigarette findLatestByUserId(Long userId);
}
