package com.addiction.challenge.missionhistory.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MissionHistoryTest {

    @DisplayName("미션을 완료하면 상태가 COMPLETED로 변경되고 완료 시간이 기록된다")
    @Test
    void complete() {
        MissionHistory missionHistory = MissionHistory.builder()
                .status(MissionStatus.PROGRESSING)
                .build();

        missionHistory.complete();

        assertThat(missionHistory.getStatus()).isEqualTo(MissionStatus.READY);
        assertThat(missionHistory.getCompleteAt()).isNotNull();
    }
}
