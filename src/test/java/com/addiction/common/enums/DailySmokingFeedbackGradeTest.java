package com.addiction.common.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DailySmokingFeedbackGradeTest {

    @Test
    void 기존_세부_규칙을_A부터_D까지_등급으로_매핑한다() {
        assertThat(DailySmokingFeedbackGrade.from(0, 0)).isEqualTo(DailySmokingFeedbackGrade.A);
        assertThat(DailySmokingFeedbackGrade.from(3, 13)).isEqualTo(DailySmokingFeedbackGrade.B);
        assertThat(DailySmokingFeedbackGrade.from(5, 5)).isEqualTo(DailySmokingFeedbackGrade.C);
        assertThat(DailySmokingFeedbackGrade.from(10, 3)).isEqualTo(DailySmokingFeedbackGrade.D);
        assertThat(DailySmokingFeedbackGrade.from(16, 2)).isEqualTo(DailySmokingFeedbackGrade.D);
    }
}
