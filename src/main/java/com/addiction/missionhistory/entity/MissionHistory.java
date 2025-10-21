package com.addiction.missionhistory.entity;

import com.addiction.challenge.entity.Challenge;
import com.addiction.common.enums.ChallengeStatus;
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
    public MissionHistory(Long id, Mission missionId, Challenge challengeId, ChallengeStatus status, Integer accTime, String address, User userId) {
        this.id = id;
        this.missionId = missionId;
        this.challengeId = challengeId;
        this.status = status;
        this.accTime = accTime;
        this.address = address;
        this.userId = userId;
    }

    public static MissionHistory createMissionHistory(Mission missionId, Challenge challengeId, ChallengeStatus status, Integer accTime, String address, User userId) {
        return MissionHistory.builder()
                .missionId(missionId)
                .challengeId(challengeId)
                .status(status)
                .accTime(accTime)
                .address(address)
                .userId(userId)
                .build();
    }
}
