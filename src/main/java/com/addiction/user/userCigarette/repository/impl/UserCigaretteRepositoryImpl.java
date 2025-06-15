package com.addiction.user.userCigarette.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.repository.UserCigaretteJpaRepository;
import com.addiction.user.userCigarette.repository.UserCigaretteRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCigaretteRepositoryImpl implements UserCigaretteRepository {

	private final UserCigaretteJpaRepository userCigaretteJpaRepository;

	@Override
	public UserCigarette save(UserCigarette userCigarette) {
		return userCigaretteJpaRepository.save(userCigarette);
	}

	@Override
	public Optional<UserCigarette> findById(int id) {
		return userCigaretteJpaRepository.findById(id);
	}

	@Override
	public Optional<UserCigarette> findByUserId(int userId) {
		return userCigaretteJpaRepository.findByUserId(userId);
	}

	@Override
	public void deleteAllInBatch() {
		userCigaretteJpaRepository.deleteAllInBatch();
	}
}
