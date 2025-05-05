package com.addiction.user.push.repository.impl;

import org.springframework.stereotype.Repository;

import com.addiction.global.exception.AddictionException;
import com.addiction.user.push.entity.Push;
import com.addiction.user.push.repository.PushJpaRepository;
import com.addiction.user.push.repository.PushRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PushRepositoryImpl implements PushRepository {

	private final PushJpaRepository pushJpaRepository;

	@Override
	public void deleteAllByUserId(int userId) {
		pushJpaRepository.deleteAllByUserId(userId);
	}

	@Override
	public Push findByDeviceIdAndUserId(String deviceId, int userId) {
		return pushJpaRepository.findByDeviceIdAndUserId(deviceId, userId)
			.orElseThrow(() -> new AddictionException("Push 데이터가 존재하지 않습니다."));
	}

	@Override
	public void deleteAllInBatch() {
		pushJpaRepository.deleteAllInBatch();
	}
}
