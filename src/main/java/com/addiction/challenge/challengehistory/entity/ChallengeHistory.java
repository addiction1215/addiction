package com.addiction.challenge.challengehistory.entity;

import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.global.BaseTimeEntity;
import com.addiction.global.exception.AddictionException;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChallengeHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    @Builder
    public ChallengeHistory(Long id, Challenge challenge, User user, ChallengeStatus status) {
        this.id = id;
        this.challenge = challenge;
        this.user = user;
        this.status = status;
    }

    public void updateStatus(ChallengeStatus status) {
        this.status = status;
    }

    public void confirmUser(Long userId) {
        if (!user.getId().equals(userId)) {
            throw new AddictionException("본인의 챌린지만 포기할 수 있습니다.");
        }
    }

    public void confirmCancel() {
        if (status == ChallengeStatus.CANCELLED) {
            throw new AddictionException("이미 포기한 챌린지입니다.");
        }
    }

    public void confirmComplete() {
        if (status == ChallengeStatus.COMPLETED) {
            throw new AddictionException("이미 완료된 챌린지는 포기할 수 없습니다.");
        }
    }

    public static ChallengeHistory createEntity(Challenge challenge, User user, ChallengeStatus status) {
        return ChallengeHistory.builder()
                .challenge(challenge)
                .user(user)
                .status(status)
                .build();
    }
}
