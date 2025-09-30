package com.addiction.user.userCigarette.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.addiction.user.userCigarette.entity.UserCigarette;

public interface UserCigaretteRepository {

	UserCigarette save(UserCigarette userCigarette);

	Optional<UserCigarette> findById(Long id);

	Optional<UserCigarette> findByUserId(Long userId);

	void deleteAllInBatch();

	List<UserCigarette> findAll();

	void deleteLastest(Long userId);

	int cigaretteCountByUserId(Long userId);

	List<UserCigarette> findAllByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

	Optional<UserCigarette> findTopByUserIdOrderByCreatedDateDesc(Long userId);

}
