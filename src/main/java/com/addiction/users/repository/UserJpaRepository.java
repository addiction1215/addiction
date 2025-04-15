package com.addiction.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.users.entity.User;

public interface UserJpaRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

}
