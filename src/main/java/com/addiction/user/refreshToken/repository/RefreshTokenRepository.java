package com.addiction.user.refreshToken.repository;

import com.addiction.user.refreshToken.entity.RefreshToken;

public interface RefreshTokenRepository {
	void deleteAllByUserId(int userId);

	RefreshToken findByUserIdAndDeviceIdAndRefreshToken(int userId, String deviceId, String refreshToken);

	void deleteAllInBatch();
}
