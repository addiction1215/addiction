package com.addiction.user.push.repository;


import com.addiction.user.push.entity.Push;

public interface PushRepository {
	void deleteAllByUserId(int userId);
	Push findByDeviceIdAndUserId(String deviceId, int userId);
	void deleteAllInBatch();
}
