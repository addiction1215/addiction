package com.addiction.user.refreshToken.repository.impl;

import org.springframework.stereotype.Repository;

import com.addiction.global.exception.AddictionException;
import com.addiction.user.refreshToken.entity.RefreshToken;
import com.addiction.user.refreshToken.repository.RefreshTokenJpaRepository;
import com.addiction.user.refreshToken.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

	private final RefreshTokenJpaRepository refreshTokenJpaRepository;

	@Override
	public RefreshToken findByUserIdAndDeviceIdAndRefreshToken(Long userId, String deviceId, String refreshToken) {
		return refreshTokenJpaRepository.findByUserIdAndDeviceIdAndRefreshToken(userId, deviceId, refreshToken)
			.orElseThrow(() -> new AddictionException("존재하지 않는 RefreshToken 입니다."));
	}

	@Override
	public void deleteAllInBatch() {
		refreshTokenJpaRepository.deleteAllInBatch();
	}
}
