package com.addiction.users.repository;

import com.addiction.users.entity.User;

public interface UserRepository {
	User save(User user);

	User findByEmail(String email);

	void deleteAllInBatch();
}
