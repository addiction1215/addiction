package com.addiction.user.userCigaretteHistory.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.user.userCigaretteHistory.entity.UserCigaretteHistory;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.userCigaretteHistory.service.request.UserCigaretteHistoryServiceRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCigaretteHistoryServiceImpl implements UserCigaretteHistoryService {

	private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;

	@Override
	public Long save(UserCigaretteHistoryServiceRequest userCigaretteHistoryServiceRequest) {
		LocalDateTime yesterday = LocalDate.now().minusDays(1).atStartOfDay();

		return userCigaretteHistoryRepository.save(
			UserCigaretteHistory.createEntity(
				userCigaretteHistoryServiceRequest.getUser(),
				userCigaretteHistoryServiceRequest.getCount(),
				yesterday
			)
		).getId();
	}
}
