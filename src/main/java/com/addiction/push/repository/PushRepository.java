package com.addiction.push.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.push.entity.Push;

public interface PushRepository extends JpaRepository<Push, Integer> {
    void deleteAllByUserId(int userId);
    Optional<Push> findByDeviceIdAndUserId(String deviceId, int userId);
    
}
