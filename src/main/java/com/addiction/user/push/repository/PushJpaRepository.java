package com.addiction.user.push.repository;

import com.addiction.user.push.entity.Push;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushJpaRepository extends JpaRepository<Push, Long> {
}
