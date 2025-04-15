package com.addiction.users.repository;

import org.springframework.stereotype.Repository;

import com.addiction.global.exception.AddictionException;
import com.addiction.users.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		return userJpaRepository.findByEmail(email)
			.orElseThrow(() -> new AddictionException("해당 회원은 존재하지 않습니다."));
	}

	@Override
	public void deleteAllInBatch() {
		userJpaRepository.deleteAllInBatch();
	}
}
