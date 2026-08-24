package com.addiction.common.enums;

/**
 * 정기 흡연 피드백의 행동 문구 시간대입니다.
 *
 * <p>사용자가 설정한 발송 슬롯에 따라 해당 시간대의 행동 문구 풀을 선택합니다.</p>
 */
public enum DailySmokingFeedbackTime {
    MORNING, // 08:45 발송: 오전 행동 문구 선택
    LUNCH,   // 12:30 발송: 점심 행동 문구 선택
    DINNER,  // 18:30 발송: 저녁 행동 문구 선택
    NIGHT;   // 21:00 발송: 밤 행동 문구 선택

}
