package com.addiction.user.users.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.repository.UserJpaRepository;
import com.addiction.user.users.repository.UserRepository;

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
	public Optional<User> findByEmail(String email) {
		return userJpaRepository.findByEmail(email);
	}

	@Override
	public Optional<User> findById(int id) {
		return userJpaRepository.findById(id);
	}

	@Override
	public void deleteAllInBatch() {
		userJpaRepository.deleteAllInBatch();
	}
}
