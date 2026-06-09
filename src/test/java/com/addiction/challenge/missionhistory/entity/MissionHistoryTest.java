package com.addiction.challenge.missionhistory.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MissionHistoryTest {

    @DisplayName("미션 제출 조건을 만족하면 승인 대기 상태로 변경된다")
    @Test
    void markReady() {
        MissionHistory missionHistory = MissionHistory.builder()
                .status(MissionStatus.PROGRESSING)
                .build();

        missionHistory.markReady();

        assertThat(missionHistory.getStatus()).isEqualTo(MissionStatus.READY);
        assertThat(missionHistory.getCompleteAt()).isNull();
    }

    @DisplayName("승인 대기 미션을 승인하면 완료 상태로 변경되고 완료 시간이 기록된다")
    @Test
    void approve() {
        MissionHistory missionHistory = MissionHistory.builder()
                .status(MissionStatus.READY)
                .build();

        missionHistory.approve();

        assertThat(missionHistory.getStatus()).isEqualTo(MissionStatus.COMPLETED);
        assertThat(missionHistory.getCompleteAt()).isNotNull();
    }
}
