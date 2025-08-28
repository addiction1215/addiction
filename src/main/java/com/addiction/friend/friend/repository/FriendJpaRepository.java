package com.addiction.friend.friend.repository;

import com.addiction.friend.friend.entity.Friend;
import com.addiction.friend.friend.entity.FriendStatus;
import com.addiction.user.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendJpaRepository extends JpaRepository<Friend, Long> {
    boolean existsByRequesterAndReceiver(User requester, User receiver);
    
    @Query("SELECT f FROM Friend f WHERE f.requester = :requester AND f.receiver = :receiver AND f.status = :status")
    Optional<Friend> findByRequesterAndReceiverAndStatus(@Param("requester") User requester, 
                                                        @Param("receiver") User receiver, 
                                                        @Param("status") FriendStatus status);
}
