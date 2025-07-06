package com.addiction.user.userCigarette.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.addiction.user.userCigarette.entity.UserCigarette;

public interface UserCigaretteJpaRepository extends JpaRepository<UserCigarette, Integer> {

	Optional<UserCigarette> findByUserId(int userId);

	@Modifying
	@Query(value = "DELETE FROM user_cigarette WHERE id = (SELECT id FROM user_cigarette WHERE user_id = :userId ORDER BY id DESC LIMIT 1)", nativeQuery = true)
	void deleteLatestByUserId(@Param("userId") int userId);

	int countByUserId(int userId);

	List<UserCigarette> findAllByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

	Optional<UserCigarette> findTopByUserIdOrderByCreatedDateDesc(int userId);
}
