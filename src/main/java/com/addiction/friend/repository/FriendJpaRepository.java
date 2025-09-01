package com.addiction.friend.repository;

import com.addiction.friend.entity.Friend;
import com.addiction.friend.entity.Friends;
import com.addiction.friend.entity.FriendStatus;
import com.addiction.user.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendJpaRepository extends JpaRepository<Friend, Long> {
    boolean existsByRequesterAndReceiver(User requester, User receiver);
    
    @Query("SELECT f FROM Friend f WHERE f.requester = :requester AND f.receiver = :receiver AND f.status = :status")
    Optional<Friend> findByRequesterAndReceiverAndStatus(@Param("requester") User requester, 
                                                        @Param("receiver") User receiver, 
                                                        @Param("status") FriendStatus status);
    
    @Query("SELECT f FROM Friend f WHERE " +
           "((f.requester = :user1 AND f.receiver = :user2) OR " +
           "(f.requester = :user2 AND f.receiver = :user1)) AND f.status = 'ACCEPTED'")
    List<Friend> findAcceptedFriendshipBetween(@Param("user1") User user1, @Param("user2") User user2);
    
    @Query("SELECT f FROM Friend f WHERE " +
           "((f.requester = :user) OR (f.receiver = :user)) AND f.status = 'ACCEPTED'")
    List<Friend> findAllAcceptedFriendshipsBy(@Param("user") User user);

    default Friends findAcceptedFriendshipsBetween(User user1, User user2) {
        return Friends.from(findAcceptedFriendshipBetween(user1, user2));
    }

    default Friends findAllAcceptedFriendshipsOfUser(User user) {
        return Friends.from(findAllAcceptedFriendshipsBy(user));
    }
}
