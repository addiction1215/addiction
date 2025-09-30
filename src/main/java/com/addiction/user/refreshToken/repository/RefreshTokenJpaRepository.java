package com.addiction.user.refreshToken.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.user.refreshToken.entity.RefreshToken;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByUserIdAndDeviceIdAndRefreshToken(Long userId, String deviceId, String refreshToken);
}
