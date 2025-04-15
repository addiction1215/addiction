package com.addiction.push.repository;


import com.addiction.push.entity.Push;

public interface PushRepository {
	void deleteAllByUserId(int userId);
	Push findByDeviceIdAndUserId(String deviceId, int userId);
	void deleteAllInBatch();
}
