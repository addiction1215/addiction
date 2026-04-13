package com.addiction.user.users.repository;

import com.addiction.user.users.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthJpaRepository extends JpaRepository<EmailAuth, Long> {
}
