package com.addiction.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 매일 아침 7시에 전송되는 흡연 패턴 피드백 메시지
 * 전날 흡연 데이터(평균 흡연 빈도, 평균 금연 유지 시간)를 기준으로 적절한 메시지를 선택
 */
@Getter
@RequiredArgsConstructor
public enum DailySmokingPushMessage {

    // 0. 평균 흡연빈도 0회
    PERFECT(
            null, null,
            "완벽해요! 👏 하지만 방심하지 마세요, 흡연 위협은 주변에 얼마든지 있으니까요."
    ),

    // 1. 평균 흡연빈도 3번 이하 + 평균 금연 유지 시간 20시간 초과
    EXCELLENT(
            3, 20L,
            "잘하고 있어요! 이제 더 줄여서 아예 끊어보는 건 어떨까요?"
    ),

    // 2. 평균 흡연 빈도 3번 이하 + 평균 금연 유지 시간 20시간 이하
    GOOD_EXTEND_TIME(
            3, 20L,
            "좋아요! 👍 금연 유지 시간을 조금 더 늘려보는 걸 함께 도전해봐요."
    ),

    // 3. 평균 흡연 빈도 3번 이하 + 평균 금연 유지 시간 16시간 이하
    KEEP_EFFORT(
            3, 16L,
            "꾸준히 노력 중이군요. 💪 하루 반나절 이상은 담배 없이 버틸 수 있도록 훈련해봐요."
    ),

    // 4. 평균 흡연 빈도 3번 이하 + 평균 금연 유지 시간 12시간 이하
    GOOD_FLOW(
            3, 12L,
            "좋은 흐름이에요. 🌙 아침부터 저녁까지 금연 유지에 도전해보세요."
    ),

    // 5. 평균 흡연 빈도 5번 이하 + 평균 금연 유지 시간 12시간 이하
    DOING_WELL(
            5, 12L,
            "꽤 잘하고 있어요! 이제 하루 절반 이상을 금연 구간으로 늘려봅시다."
    ),

    // 6. 평균 흡연 빈도 5번 이하 + 평균 금연 유지 시간 8시간 이하
    GOOD_START(
            5, 8L,
            "시작은 좋아요. 🚀 8시간을 10시간, 12시간으로 늘릴 수 있도록 조금만 더 밀어붙여봐요."
    ),

    // 7. 평균 흡연 빈도 5번 이하 + 평균 금연 유지 시간 4시간 이하
    SHORT_INTERVAL(
            5, 4L,
            "짧은 간격으로 흡연하고 있네요. 😶 담배 없는 반나절을 만드는 게 이번 주 목표입니다."
    ),

    // 8. 평균 흡연 빈도 10번 이하 + 평균 금연 유지 시간 8시간 이하
    NOT_BAD(
            10, 8L,
            "나쁘지 않아요. 👍 하지만 하루 중 긴 시간은 담배 없이 보내는 습관을 들여야 합니다."
    ),

    // 9. 평균 흡연 빈도 10번 이하 + 평균 금연 유지 시간 4시간 이하
    TOO_SHORT_INTERVAL(
            10, 4L,
            "흡연 간격이 너무 짧습니다. ⏱️ 최소 반나절은 끊어내는 습관을 훈련해봐요."
    ),

    // 10. 평균 흡연 빈도 10번 이하 + 평균 금연 유지 시간 2시간 이하
    VERY_SHORT_INTERVAL(
            10, 2L,
            "금연 유지 시간이 매우 짧습니다. 🚨 담배 간격을 조금만 벌려도 큰 변화가 시작됩니다."
    ),

    // 11. 평균 흡연 빈도 15번 이하 + 평균 금연 유지 시간 4시간 이하
    HIGH_FREQUENCY(
            15, 4L,
            "하루 흡연량이 많습니다. 🚭 흡연 횟수를 우선 줄이는 전략이 필요해요."
    ),

    // 12. 평균 흡연 빈도 15번 이하 + 평균 금연 유지 시간 2시간 이하
    WARNING(
            15, 2L,
            "경고 🚨: 흡연 빈도와 간격이 모두 위험 수준입니다. 지금이 바로 줄이기를 시작해야 할 시점이에요."
    ),

    // 13. 평균 흡연 빈도 15번 초과 + 평균 금연 유지 시간 4시간 이하
    VERY_HIGH_FREQUENCY(
            15, 4L,
            "하루 대부분을 담배와 함께하고 있습니다. ⚠️ 건강에 심각한 부담이 쌓이고 있어요. 오늘부터 줄이는 실천을 시작해야 합니다."
    ),

    // 14. 평균 흡연 빈도 15번 초과 + 평균 금연 유지 시간 2시간 이하
    CRITICAL(
            null, 2L,
            "매우 위험한 패턴입니다. 🚫 평균 흡연 간격이 2시간 이하라면, 몸이 휴식할 틈도 없습니다. 지금 바로 금연 행동 계획을 세워야 합니다."
    );

    private final Integer maxSmokeCount; // 최대 흡연 빈도 (null이면 무제한)
    private final Long maxAvgPatienceTime; // 최대 평균 금연 유지 시간 (시간 단위, null이면 무제한)
    private final String message; // 전송할 메시지

    /**
     * 전날 흡연 데이터를 기반으로 적절한 메시지를 선택
     * 우선순위 순서대로 조건을 체크하여 가장 먼저 매칭되는 메시지를 반환
     *
     * @param smokeCount      평균 흡연 빈도
     * @param avgPatienceTime 평균 금연 유지 시간 (시간 단위)
     * @return 선택된 메시지 Enum
     */
    public static DailySmokingPushMessage selectMessage(int smokeCount, long avgPatienceTime) {
        // 0. 흡연 횟수가 0이면 PERFECT
        if (smokeCount == 0) {
            return PERFECT;
        }

        // 1. 평균 흡연빈도 3번 이하 + 평균 금연 유지 시간 20시간 초과
        if (smokeCount <= 3 && avgPatienceTime > 20) {
            return EXCELLENT;
        }

        // 2. 평균 흡연 빈도 3번 이하 + 평균 금연 유지 시간 20시간 이하
        if (smokeCount <= 3 && avgPatienceTime > 16) {
            return GOOD_EXTEND_TIME;
        }

        // 3. 평균 흡연 빈도 3번 이하 + 평균 금연 유지 시간 16시간 이하
        if (smokeCount <= 3 && avgPatienceTime > 12) {
            return KEEP_EFFORT;
        }

        // 4. 평균 흡연 빈도 3번 이하 + 평균 금연 유지 시간 12시간 이하
        if (smokeCount <= 3) {
            return GOOD_FLOW;
        }

        // 5. 평균 흡연 빈도 5번 이하 + 평균 금연 유지 시간 12시간 이하
        if (smokeCount <= 5 && avgPatienceTime <= 12 && avgPatienceTime > 8) {
            return DOING_WELL;
        }

        // 6. 평균 흡연 빈도 5번 이하 + 평균 금연 유지 시간 8시간 이하
        if (smokeCount <= 5 && avgPatienceTime <= 8 && avgPatienceTime > 4) {
            return GOOD_START;
        }

        // 7. 평균 흡연 빈도 5번 이하 + 평균 금연 유지 시간 4시간 이하
        if (smokeCount <= 5 && avgPatienceTime <= 4) {
            return SHORT_INTERVAL;
        }

        // 8. 평균 흡연 빈도 10번 이하 + 평균 금연 유지 시간 8시간 이하
        if (smokeCount <= 10 && avgPatienceTime <= 8 && avgPatienceTime > 4) {
            return NOT_BAD;
        }

        // 9. 평균 흡연 빈도 10번 이하 + 평균 금연 유지 시간 4시간 이하
        if (smokeCount <= 10 && avgPatienceTime <= 4 && avgPatienceTime > 2) {
            return TOO_SHORT_INTERVAL;
        }

        // 10. 평균 흡연 빈도 10번 이하 + 평균 금연 유지 시간 2시간 이하
        if (smokeCount <= 10 && avgPatienceTime <= 2) {
            return VERY_SHORT_INTERVAL;
        }

        // 11. 평균 흡연 빈도 15번 이하 + 평균 금연 유지 시간 4시간 이하
        if (smokeCount <= 15 && avgPatienceTime <= 4 && avgPatienceTime > 2) {
            return HIGH_FREQUENCY;
        }

        // 12. 평균 흡연 빈도 15번 이하 + 평균 금연 유지 시간 2시간 이하
        if (smokeCount <= 15 && avgPatienceTime <= 2) {
            return WARNING;
        }

        // 13. 평균 흡연 빈도 15번 초과 + 평균 금연 유지 시간 4시간 이하
        if (smokeCount > 15 && avgPatienceTime <= 4 && avgPatienceTime > 2) {
            return VERY_HIGH_FREQUENCY;
        }

        // 14. 평균 흡연 빈도 15번 초과 + 평균 금연 유지 시간 2시간 이하 (나머지 모든 경우)
        return CRITICAL;
    }
}
