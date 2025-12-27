package com.addiction.challenge.missionhistory.entity;

import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.common.enums.MissionStatus;
import com.addiction.global.BaseTimeEntity;
import com.addiction.challenge.mission.entity.Mission;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MissionHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChallengeHistory challengeHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mission mission;

    @Enumerated(EnumType.STRING)
    private MissionStatus status;

    private LocalDateTime completeAt;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public MissionHistory(Long id, Mission mission, ChallengeHistory challengeHistory, MissionStatus status, LocalDateTime completeAt, String address, User user) {
        this.id = id;
        this.mission = mission;
        this.challengeHistory = challengeHistory;
        this.status = status;
        this.completeAt = completeAt;
        this.address = address;
        this.user = user;
    }
}
