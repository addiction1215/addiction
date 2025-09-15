package com.addiction.user.users.repository;

import java.util.List;
import java.util.Optional;

import com.addiction.user.users.entity.User;

public interface UserRepository {
	User save(User user);

	Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndNickName(String email, String nickName);

	Optional<User> findById(Long id);

	void deleteAllInBatch();

	void saveAll(List<User> users);
}
