package com.addiction.missionhistory.entity;

import com.addiction.challengehistory.entity.ChallengeHistory;
import com.addiction.common.enums.MissionStatus;
import com.addiction.global.BaseTimeEntity;
import com.addiction.mission.entity.Mission;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private MissionStatus status;

    @Column(name = "acc_time")
    private Integer accTime;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public MissionHistory(Long id, Mission mission, ChallengeHistory challengeHistory, MissionStatus status, Integer accTime, String address, User user) {
        this.id = id;
        this.mission = mission;
        this.challengeHistory = challengeHistory;
        this.status = status;
        this.accTime = accTime;
        this.address = address;
        this.user = user;
    }
}
