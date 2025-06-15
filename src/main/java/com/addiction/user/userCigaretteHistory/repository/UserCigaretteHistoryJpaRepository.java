package com.addiction.user.userCigaretteHistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.user.userCigaretteHistory.entity.UserCigaretteHistory;

public interface UserCigaretteHistoryJpaRepository extends JpaRepository<UserCigaretteHistory, Long> {
}
