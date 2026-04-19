package com.addiction.user.push.repository;

import com.addiction.user.push.entity.Push;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PushJpaRepository extends JpaRepository<Push, Long> {

    Optional<Push> findByDeviceId(String deviceId);
}
