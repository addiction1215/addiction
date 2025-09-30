package com.addiction.user.push.repository;


import java.util.List;

import com.addiction.user.push.entity.Push;

public interface PushRepository {
	Push findByDeviceIdAndUserId(String deviceId, Long userId);
	void deleteAllInBatch();
	Push save(Push push);
	void saveAll(List<Push> pushes);
}
