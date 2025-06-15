package com.addiction.user.userCigaretteHistory.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.addiction.user.userCigaretteHistory.entity.UserCigaretteHistory;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryJpaRepository;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCigaretteHistoryRepositoryImpl implements UserCigaretteHistoryRepository {

	private final UserCigaretteHistoryJpaRepository userCigaretteHistoryJpaRepository;

	@Override
	public UserCigaretteHistory save(UserCigaretteHistory cigaretteHistory) {
		return userCigaretteHistoryJpaRepository.save(cigaretteHistory);
	}

	@Override
	public Optional<UserCigaretteHistory> findById(Long id) {
		return userCigaretteHistoryJpaRepository.findById(id);
	}

	@Override
	public void deleteAllInBatch() {
		userCigaretteHistoryJpaRepository.deleteAllInBatch();
	}
}
