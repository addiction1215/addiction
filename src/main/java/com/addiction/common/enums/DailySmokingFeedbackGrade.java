package com.addiction.common.enums;

/**
 * 정기 흡연 피드백의 사용자 상태 등급입니다.
 *
 * <p>세부 판정 규칙 0~14는 기존 기준을 유지하며, 발송 문구는 이 네 등급으로만 구분합니다.</p>
 */
public enum DailySmokingFeedbackGrade {
    A, B, C, D;

    public static DailySmokingFeedbackGrade from(int smokeCount, long avgPatienceTime) {
        if (smokeCount == 0) {
            return A; // 기존 규칙 0
        }

        if (smokeCount <= 3 && avgPatienceTime > 20) return B; // 규칙 1
        if (smokeCount <= 3 && avgPatienceTime > 16) return B; // 규칙 2
        if (smokeCount <= 3 && avgPatienceTime > 12) return B; // 규칙 3
        if (smokeCount <= 3) return B; // 규칙 4

        if (smokeCount <= 5 && avgPatienceTime <= 12 && avgPatienceTime > 8) return C; // 규칙 5
        if (smokeCount <= 5 && avgPatienceTime <= 8 && avgPatienceTime > 4) return C; // 규칙 6
        if (smokeCount <= 5 && avgPatienceTime <= 4) return C; // 규칙 7
        if (smokeCount <= 10 && avgPatienceTime <= 8 && avgPatienceTime > 4) return C; // 규칙 8

        // 기존 규칙 9~14 및 기존 로직의 최종 fallback
        return D;
    }
}
