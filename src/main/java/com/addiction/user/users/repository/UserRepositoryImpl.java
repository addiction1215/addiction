package com.addiction.user.users.repository;

import org.springframework.stereotype.Repository;

import com.addiction.global.exception.AddictionException;
import com.addiction.user.users.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private static final String UNKNOWN_USER = "해당 회원은 존재하지 않습니다.";

	private final UserJpaRepository userJpaRepository;

	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userJpaRepository.findByEmail(email)
			.orElseThrow(() -> new AddictionException(UNKNOWN_USER));
	}

	@Override
	public User findById(int id) {
		return userJpaRepository.findById(id)
			.orElseThrow(() -> new AddictionException(UNKNOWN_USER));
	}

	@Override
	public void deleteAllInBatch() {
		userJpaRepository.deleteAllInBatch();
	}
}
