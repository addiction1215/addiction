package com.addiction.user.refreshToken.repository;

import com.addiction.user.refreshToken.entity.RefreshToken;

public interface RefreshTokenRepository {
	RefreshToken findByUserIdAndDeviceIdAndRefreshToken(Long userId, String deviceId, String refreshToken);

	void deleteAllInBatch();
}
