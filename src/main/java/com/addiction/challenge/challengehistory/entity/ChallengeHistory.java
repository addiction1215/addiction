package com.addiction.challenge.challengehistory.entity;

import com.addiction.challenge.challenge.entity.Challenge;
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
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "finish_yn")
    private YnStatus finishYn;

    @Builder
    public ChallengeHistory(Long id, Challenge challenge, User user, YnStatus finishYn) {
        this.id = id;
        this.challenge = challenge;
        this.user = user;
        this.finishYn = finishYn;
    }
}
