package com.addiction.challengehistory.entity;

import com.addiction.challenge.entity.Challenge;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.common.enums.YnStatus;
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
public class ChallengeHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "finish_yn")
    private YnStatus finishYn;

    @Builder
    public ChallengeHistory(Long id, Challenge challengeId, User userId, YnStatus finishYn) {
        this.id = id;
        this.challengeId = challengeId;
        this.userId = userId;
        this.finishYn = finishYn;
    }
}
