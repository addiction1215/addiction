package com.addiction.user.userCigarette.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.addiction.user.userCigarette.entity.UserCigarette;

public interface UserCigaretteRepository {

	UserCigarette save(UserCigarette userCigarette);

	Optional<UserCigarette> findById(int id);

	Optional<UserCigarette> findByUserId(int userId);

	void deleteAllInBatch();

	List<UserCigarette> findAll();

	void deleteLastest(int userId);

	int cigaretteCountByUserId(int userId);

	List<UserCigarette> findAllByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

	Optional<UserCigarette> findTopByUserIdOrderByCreatedDateDesc(int userId);
}
