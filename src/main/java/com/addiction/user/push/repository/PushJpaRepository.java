package com.addiction.user.push.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.user.push.entity.Push;

public interface PushJpaRepository extends JpaRepository<Push, Integer> {
    void deleteAllByUserId(int userId);
    Optional<Push> findByDeviceIdAndUserId(String deviceId, int userId);
}
