package com.addiction.user.users.repository;

import java.util.Optional;

import com.addiction.user.users.entity.User;

public interface UserRepository {
	User save(User user);

	Optional<User> findByEmail(String email);

	Optional<User> findById(int id);

	void deleteAllInBatch();
}
