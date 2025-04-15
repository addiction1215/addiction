package com.addiction.user.users.repository;

import com.addiction.user.users.entity.User;

public interface UserRepository {
	User save(User user);

	User findByEmail(String email);

	void deleteAllInBatch();
}
