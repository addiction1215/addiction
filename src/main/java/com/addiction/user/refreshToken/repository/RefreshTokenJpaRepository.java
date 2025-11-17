package com.addiction.user.refreshToken.repository;

import com.addiction.user.refreshToken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
}
