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
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;

    private ChallengeStatus status;

    @Column(name = "acc_time")
    private Integer accTime;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public MissionHistory(Long id, Mission mission, Challenge challenge, Integer accTime, String address, User user) {
        this.id = id;
        this.mission = mission;
        this.challenge = challenge;
        this.accTime = accTime;
        this.address = address;
        this.user = user;
    }
}
