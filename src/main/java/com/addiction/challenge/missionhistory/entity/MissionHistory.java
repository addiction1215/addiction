package com.addiction.challenge.missionhistory.entity;

import com.addiction.challenge.challenge.entity.Challenge;
import com.addiction.common.enums.ChallengeStatus;
import com.addiction.global.BaseTimeEntity;
import com.addiction.challenge.mission.entity.Mission;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.Builder;

public class MissionHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission missionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;

    private ChallengeStatus status;

    @Column(name = "acc_time")
    private Integer accTime;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Builder
    public MissionHistory(Long id, Mission missionId, Challenge challengeId, Integer accTime, String address, User userId) {
        this.id = id;
        this.missionId = missionId;
        this.challengeId = challengeId;
        this.accTime = accTime;
        this.address = address;
        this.userId = userId;
    }
}
