package com.addiction.refreshToken.repository;

import com.addiction.refreshToken.entity.RefreshToken;

public interface RefreshTokenRepository {
	void deleteAllByUserId(int userId);

	RefreshToken findByUserIdAndDeviceIdAndRefreshToken(int userId, String deviceId, String refreshToken);

	void deleteAllInBatch();
}
