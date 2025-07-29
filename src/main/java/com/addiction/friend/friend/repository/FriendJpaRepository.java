package com.addiction.friend.friend.repository;

import com.addiction.friend.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendJpaRepository extends JpaRepository<Friend, Long> {

}
