package com.addiction.user.refreshToken.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.addiction.user.refreshToken.entity.RefreshToken;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT r FROM RefreshToken r WHERE r.user.id = :userId AND r.deviceId = :deviceId AND r.refreshToken = :refreshToken")
    Optional<RefreshToken> findByUserIdAndDeviceIdAndRefreshToken(
            @Param("userId") Long userId,
            @Param("deviceId") String deviceId,
            @Param("refreshToken") String refreshToken
    );
}
