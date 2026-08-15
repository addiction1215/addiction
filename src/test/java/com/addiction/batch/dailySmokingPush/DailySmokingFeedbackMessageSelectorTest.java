package com.addiction.batch.dailySmokingPush;

import com.addiction.common.enums.DailySmokingFeedbackGrade;
import com.addiction.common.enums.DailySmokingFeedbackTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DailySmokingFeedbackMessageSelectorTest {

    @Test
    void 등급과_시간대에_맞는_상태_및_행동_문구를_선택한다() {
        DailySmokingFeedbackMessageSelector selector = new DailySmokingFeedbackMessageSelector(new ObjectMapper());
        selector.loadMessagePool();

        DailySmokingFeedbackContent content = selector.select(
                DailySmokingFeedbackGrade.C,
                DailySmokingFeedbackTime.LUNCH
        );

        assertThat(content.statusMessage()).isNotBlank();
        assertThat(content.actionMessage()).isNotBlank();
        assertThat(content.toPushBody()).contains("\n");
    }
}
