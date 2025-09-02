package com.addiction.friend.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Friend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    @Builder
    private Friend(User requester, User receiver, FriendStatus status) {
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
    }

    public static Friend createRequest(User requester, User receiver) {
        return Friend.builder()
                .requester(requester)
                .receiver(receiver)
                .status(FriendStatus.PENDING)
                .build();
    }

    public void accept() {
        this.status = FriendStatus.ACCEPTED;
    }

    public void reject() {
        this.status = FriendStatus.REJECTED;
    }

    public boolean isPending() {
        return this.status == FriendStatus.PENDING;
    }

    public boolean isAccepted() {
        return this.status == FriendStatus.ACCEPTED;
    }

    public boolean isFriendshipBetween(User user1, User user2) {
        return (this.requester.equals(user1) && this.receiver.equals(user2)) ||
               (this.requester.equals(user2) && this.receiver.equals(user1));
    }

    public boolean isParticipant(User user) {
        return this.requester.equals(user) || this.receiver.equals(user);
    }

    public boolean canBeDeletedBy(User user) {
        return this.isAccepted() && this.isParticipant(user);
    }

    public User getOtherParticipant(User user) {
        if (this.requester.equals(user)) {
            return this.receiver;
        } else if (this.receiver.equals(user)) {
            return this.requester;
        }
        throw new IllegalArgumentException("해당 사용자는 이 친구 관계의 참여자가 아닙니다.");
    }
}
