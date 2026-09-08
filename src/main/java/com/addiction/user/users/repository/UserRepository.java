package com.addiction.user.users.repository;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

import com.addiction.user.users.entity.User;

public interface UserRepository {
    List<User> findAll();

    List<User> findAllWithPushes();

	User save(User user);

	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

    boolean markFirstSmokingRecorded(Long userId, LocalDateTime recordedAt);

	void deleteAllInBatch();

	void saveAll(List<User> users);

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);
}
