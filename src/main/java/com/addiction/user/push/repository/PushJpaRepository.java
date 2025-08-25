package com.addiction.user.push.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.user.push.entity.Push;

public interface PushJpaRepository extends JpaRepository<Push, Long> {
    void deleteAllByUserId(Long userId);
    Optional<Push> findByDeviceIdAndUserId(String deviceId, Long userId);
}
