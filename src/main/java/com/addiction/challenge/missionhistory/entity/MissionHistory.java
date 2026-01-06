package com.addiction.challenge.missionhistory.entity;

import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.mission.entity.Mission;
import com.addiction.common.enums.MissionStatus;
import com.addiction.global.BaseTimeEntity;
import com.addiction.global.exception.AddictionException;
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

    // 미션 제출 관련 컬럼들
    private Integer gpsVerifyCount;     // GPS 검증 횟수
    private String photoUrl1;           // 사진 URL 1
    private String photoUrl2;           // 사진 URL 2
    private String photoUrl3;           // 사진 URL 3
    private Integer abstinenceTime;     // 금연 시간 (초 단위)

    @Builder
    public MissionHistory(Long id, Mission mission, ChallengeHistory challengeHistory, MissionStatus status,
                          LocalDateTime completeAt, String address, User user,
                          Integer gpsVerifyCount, String photoUrl1, String photoUrl2, String photoUrl3, Integer abstinenceTime) {
        this.id = id;
        this.mission = mission;
        this.challengeHistory = challengeHistory;
        this.status = status;
        this.completeAt = completeAt;
        this.address = address;
        this.user = user;
        this.gpsVerifyCount = gpsVerifyCount;
        this.photoUrl1 = photoUrl1;
        this.photoUrl2 = photoUrl2;
        this.photoUrl3 = photoUrl3;
        this.abstinenceTime = abstinenceTime;
    }

    public static MissionHistory createEntity(Mission mission, ChallengeHistory challengeHistory, User user, MissionStatus status) {
        return MissionHistory.builder()
                .mission(mission)
                .challengeHistory(challengeHistory)
                .status(status)
                .user(user)
                .gpsVerifyCount(0)
                .abstinenceTime(0)
                .build();
    }

    // 비즈니스 로직 메서드들
    public void submitGps(String address) {
        if (address == null || address.isEmpty()) {
            throw new AddictionException("GPS 미션은 주소 정보가 필요합니다.");
        }
        this.address = address;
        this.gpsVerifyCount = this.gpsVerifyCount != null ? this.gpsVerifyCount + 1 : 1;
    }

    public void submitPhoto(String photoUrl) {
        if (photoUrl == null || photoUrl.isEmpty()) {
            throw new AddictionException("사진 미션은 사진 URL이 필요합니다.");
        }

        if (this.photoUrl1 == null || this.photoUrl1.isBlank()) {
            this.photoUrl1 = photoUrl;
            return;
        }

        if (this.photoUrl2 == null || this.photoUrl2.isBlank()) {
            this.photoUrl2 = photoUrl;
            return;
        }

        if (this.photoUrl3 == null || this.photoUrl3.isBlank()) {
            this.photoUrl3 = photoUrl;
            return;
        }

        throw new IllegalArgumentException("사진 슬롯이 모두 채워져 있습니다.");
    }

    public void submitAbstinenceTime(int time) {
        if (time <= 0) {
            throw new AddictionException("흡연 참기 미션은 시간 정보가 필요합니다.");
        }
        this.abstinenceTime = time;
    }

    public void complete() {
        this.status = MissionStatus.COMPLETED;
        this.completeAt = LocalDateTime.now();
    }

    public void locationVerify() {
        if (gpsVerifyCount == null || gpsVerifyCount < 3) {
            throw new AddictionException("GPS 미션은 최소 3회 이상 검증이 필요합니다.");
        }
    }

    public void photoVerify() {
        if (photoUrl1 == null ||
                photoUrl2 == null ||
                photoUrl3 == null) {
            throw new AddictionException("사진 미션은 3장의 사진이 모두 필요합니다.");
        }
    }

    public void abstinenceTimeVerify() {
        if (abstinenceTime == null || abstinenceTime <= 0) {
            throw new AddictionException("흡연 참기 미션은 시간 정보가 필요합니다.");
        }
    }
}
