package com.addiction.common.enums;

import java.time.LocalTime;

/**
 * 정기 흡연 피드백의 행동 문구 시간대입니다.
 *
 * <p>배치는 매일 08:45, 12:30, 18:30, 21:00에 실행되며,
 * 실행 시각을 아래 시간대로 변환해 해당 시간대의 행동 문구 풀을 선택합니다.</p>
 */
public enum DailySmokingFeedbackTime {
    MORNING, // 08:45 발송: 오전 행동 문구 선택
    LUNCH,   // 12:30 발송: 점심 행동 문구 선택
    DINNER,  // 18:30 발송: 저녁 행동 문구 선택
    NIGHT;   // 21:00 발송: 밤 행동 문구 선택

    /**
     * 배치가 실제로 실행된 시각을 행동 문구 시간대로 변환합니다.
     *
     * <p>배치가 몇 초 늦게 실행되어도 올바른 문구 풀이 선택되도록,
     * 정확한 분 단위 일치가 아니라 시간 범위로 판정합니다.</p>
     *
     * <ul>
     *   <li>11:00 전: MORNING (08:45 배치)</li>
     *   <li>11:00~14:59: LUNCH (12:30 배치)</li>
     *   <li>15:00~19:59: DINNER (18:30 배치)</li>
     *   <li>20:00 이후: NIGHT (21:00 배치)</li>
     * </ul>
     */
    public static DailySmokingFeedbackTime from(LocalTime time) {
        if (time.isBefore(LocalTime.of(11, 0))) return MORNING;
        if (time.isBefore(LocalTime.of(15, 0))) return LUNCH;
        if (time.isBefore(LocalTime.of(20, 0))) return DINNER;
        return NIGHT;
    }
}
