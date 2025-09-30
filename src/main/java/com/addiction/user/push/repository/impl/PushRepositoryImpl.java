package com.addiction.user.push.repository.impl;

import java.util.List;

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
	public void deleteAllInBatch() {
		pushJpaRepository.deleteAllInBatch();
	}

	@Override
	public Push save(Push push) {
		return pushJpaRepository.save(push);
	}

	@Override
	public void saveAll(List<Push> pushes) {
		pushJpaRepository.saveAll(pushes);
	}
}
