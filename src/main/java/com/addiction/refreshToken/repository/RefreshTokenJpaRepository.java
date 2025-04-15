package com.addiction.refreshToken.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.refreshToken.entity.RefreshToken;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Integer> {
	void deleteAllByUserId(int userId);

	Optional<RefreshToken> findByUserIdAndDeviceIdAndRefreshToken(int userId, String deviceId, String refreshToken);
}
