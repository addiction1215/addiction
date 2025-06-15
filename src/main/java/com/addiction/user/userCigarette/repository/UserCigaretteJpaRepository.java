package com.addiction.user.userCigarette.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.user.userCigarette.entity.UserCigarette;

public interface UserCigaretteJpaRepository extends JpaRepository<UserCigarette, Integer> {

	Optional<UserCigarette> findByUserId(int userId);
}
