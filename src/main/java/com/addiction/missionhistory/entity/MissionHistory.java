package com.addiction.missionhistory.entity;

import com.addiction.challenge.entity.Challenge;
import com.addiction.common.enums.MissionStatus;
import com.addiction.global.BaseTimeEntity;
import com.addiction.mission.entity.Mission;
import com.addiction.user.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    private MissionStatus status;

    @Column(name = "acc_time")
    private Integer accTime;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Builder
    public MissionHistory(Long id, Mission missionId, Challenge challengeId, MissionStatus status, Integer accTime, String address, User userId) {
        this.id = id;
        this.missionId = missionId;
        this.challengeId = challengeId;
        this.status = status;
        this.accTime = accTime;
        this.address = address;
        this.userId = userId;
    }

    public static MissionHistory createMissionHistory(Mission missionId, Challenge challengeId, MissionStatus status, Integer accTime, String address, User userId) {
        return MissionHistory.builder()
                .missionId(missionId)
                .challengeId(challengeId)
                .status(status)
                .accTime(accTime)
                .address(address)
                .userId(userId)
                .build();
    }

	public static MissionHistory updateMissionHistoryForFail(Challenge challengeId, MissionStatus status, User userId) {
		return MissionHistory.builder()
				.challengeId(challengeId)
				.status(status)
				.userId(userId)
				.build();
	}
}
