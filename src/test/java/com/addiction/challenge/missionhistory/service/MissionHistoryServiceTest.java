package com.addiction.challenge.missionhistory.service;

import com.addiction.IntegrationTestSupport;
import com.addiction.challenge.challange.entity.Challenge;
import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.entity.ChallengeStatus;
import com.addiction.challenge.mission.entity.Mission;
import com.addiction.challenge.mission.entity.MissionCategoryStatus;
import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
import com.addiction.challenge.missionhistory.service.request.MissionSubmitServiceRequest;
import com.addiction.global.exception.AddictionException;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.SnsType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionHistoryServiceTest extends IntegrationTestSupport {

    @Autowired
    private MissionHistoryService missionHistoryService;

    @DisplayName("흡연 참기 미션 제출 조건이 충족되면 READY로 변경되고 완료 시간은 기록하지 않는다.")
    @Test
    void submitHoldMissionMarksReady() {
        MissionHistory missionHistory = saveMissionHistory(MissionCategoryStatus.HOLD);

        missionHistoryService.submitMission(MissionSubmitServiceRequest.builder()
                .missionHistoryId(missionHistory.getId())
                .time(3600)
                .build());

        MissionHistory foundMissionHistory = missionHistoryJpaRepository.findById(missionHistory.getId()).orElseThrow();
        assertThat(foundMissionHistory.getStatus()).isEqualTo(MissionStatus.READY);
        assertThat(foundMissionHistory.getCompleteAt()).isNull();
    }

    @DisplayName("GPS 미션은 3회 제출되면 READY로 변경된다.")
    @Test
    void submitLocationMissionMarksReadyAfterThreeSubmissions() {
        MissionHistory missionHistory = saveMissionHistory(MissionCategoryStatus.LOCATION);

        submitLocation(missionHistory.getId(), "주소1");
        submitLocation(missionHistory.getId(), "주소2");

        MissionHistory progressingMissionHistory = missionHistoryJpaRepository.findById(missionHistory.getId()).orElseThrow();
        assertThat(progressingMissionHistory.getStatus()).isEqualTo(MissionStatus.PROGRESSING);

        submitLocation(missionHistory.getId(), "주소3");

        MissionHistory readyMissionHistory = missionHistoryJpaRepository.findById(missionHistory.getId()).orElseThrow();
        assertThat(readyMissionHistory.getStatus()).isEqualTo(MissionStatus.READY);
        assertThat(readyMissionHistory.getCompleteAt()).isNull();
    }

    @DisplayName("사진 미션은 3장 제출되면 READY로 변경된다.")
    @Test
    void submitPhotoMissionMarksReadyAfterThreeSubmissions() {
        MissionHistory missionHistory = saveMissionHistory(MissionCategoryStatus.REPLACE_ACTION);

        submitPhoto(missionHistory.getId(), "photo1.jpg");
        submitPhoto(missionHistory.getId(), "photo2.jpg");

        MissionHistory progressingMissionHistory = missionHistoryJpaRepository.findById(missionHistory.getId()).orElseThrow();
        assertThat(progressingMissionHistory.getStatus()).isEqualTo(MissionStatus.PROGRESSING);

        submitPhoto(missionHistory.getId(), "photo3.jpg");

        MissionHistory readyMissionHistory = missionHistoryJpaRepository.findById(missionHistory.getId()).orElseThrow();
        assertThat(readyMissionHistory.getStatus()).isEqualTo(MissionStatus.READY);
        assertThat(readyMissionHistory.getCompleteAt()).isNull();
    }

    @DisplayName("READY 미션을 승인하면 COMPLETED로 변경되고 완료 시간이 기록된다.")
    @Test
    void approveMission() {
        MissionHistory missionHistory = saveMissionHistory(MissionCategoryStatus.HOLD);
        missionHistory.submitAbstinenceTime(3600);
        missionHistory.markReady();
        missionHistoryJpaRepository.save(missionHistory);

        missionHistoryService.approveMission(missionHistory.getId());

        MissionHistory foundMissionHistory = missionHistoryJpaRepository.findById(missionHistory.getId()).orElseThrow();
        assertThat(foundMissionHistory.getStatus()).isEqualTo(MissionStatus.COMPLETED);
        assertThat(foundMissionHistory.getCompleteAt()).isNotNull();
    }

    @DisplayName("READY가 아닌 미션은 승인할 수 없다.")
    @Test
    void approveMissionWhenNotReady() {
        MissionHistory missionHistory = saveMissionHistory(MissionCategoryStatus.HOLD);

        assertThatThrownBy(() -> missionHistoryService.approveMission(missionHistory.getId()))
                .isInstanceOf(AddictionException.class)
                .hasMessage("승인 대기 상태의 미션만 승인할 수 있습니다.");
    }

    private MissionHistory saveMissionHistory(MissionCategoryStatus category) {
        User user = userRepository.save(createUser("test@test.com", "1234", SnsType.NORMAL, SettingStatus.COMPLETE));
        Challenge challenge = cChallengeJpaRepository.save(createChallenge("금연 챌린지", "금연하기", "badge.png", 100));
        ChallengeHistory challengeHistory = challengeHistoryJpaRepository.save(createChallengeHistory(challenge, user, ChallengeStatus.PROGRESSING));
        Mission mission = missionJpaRepository.save(createMission(challenge, category, "미션", "미션 내용", 10));
        MissionHistory missionHistory = createMissionHistory(challengeHistory, mission, user, MissionStatus.PROGRESSING, null);
        return missionHistoryJpaRepository.save(missionHistory);
    }

    private void submitLocation(Long missionHistoryId, String address) {
        missionHistoryService.submitMission(MissionSubmitServiceRequest.builder()
                .missionHistoryId(missionHistoryId)
                .address(address)
                .build());
    }

    private void submitPhoto(Long missionHistoryId, String photoUrl) {
        missionHistoryService.submitMission(MissionSubmitServiceRequest.builder()
                .missionHistoryId(missionHistoryId)
                .photoUrl(photoUrl)
                .build());
    }
}
