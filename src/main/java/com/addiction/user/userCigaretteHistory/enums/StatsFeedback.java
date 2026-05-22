package com.addiction.user.userCigaretteHistory.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatsFeedback {

    PERFECT("완벽해요! 👏 하지만 방심하지 마세요, 흡연 위협은 주변에 얼마든지 있으니까요."),
    EXCELLENT("잘하고 있어요! 이제 더 줄여서 아예 끊어보는 건 어떨까요?"),
    GOOD_TIME("좋아요! 👍 금연 유지 시간을 조금 더 늘려보는 걸 함께 도전해봐요."),
    STEADY("꾸준히 노력 중이군요. 💪 하루 반나절 이상은 담배 없이 버틸 수 있도록 훈련해봐요."),
    MORNING("좋은 흐름이에요. 🌙 아침부터 저녁까지 금연 유지에 도전해보세요."),
    QUITE_GOOD("꽤 잘하고 있어요! 이제 하루 절반 이상을 금연 구간으로 늘려봅시다."),
    START_GOOD("시작은 좋아요. 🚀 8시간을 10시간, 12시간으로 늘릴 수 있도록 조금만 더 밀어붙여봐요."),
    SHORT_INTERVAL("짧은 간격으로 흡연하고 있네요. 😶 담배 없는 반나절을 만드는 게 이번 주 목표입니다."),
    NOT_BAD("나쁘지 않아요. 👍 하지만 하루 중 긴 시간은 담배 없이 보내는 습관을 들여야 합니다."),
    TOO_SHORT("흡연 간격이 너무 짧습니다. ⏱️ 최소 반나절은 끊어내는 습관을 훈련해봐요."),
    VERY_SHORT("금연 유지 시간이 매우 짧습니다. 🚨 담배 간격을 조금만 벌려도 큰 변화가 시작됩니다."),
    HIGH_COUNT("하루 흡연량이 많습니다. 🚭 흡연 횟수를 우선 줄이는 전략이 필요해요."),
    WARNING("경고 🚨: 흡연 빈도와 간격이 모두 위험 수준입니다. 지금이 바로 줄이기를 시작해야 할 시점이에요."),
    DANGER("하루 대부분을 담배와 함께하고 있습니다. ⚠️ 건강에 심각한 부담이 쌓이고 있어요. 오늘부터 줄이는 실천을 시작해야 합니다."),
    EXTREME("매우 위험한 패턴입니다. 🚫 평균 흡연 간격이 2시간 이하라면, 몸이 휴식할 틈도 없습니다. 지금 바로 금연 행동 계획을 세워야 합니다.");

    private final String message;

    private static final double HOURS_20 = 72000;
    private static final double HOURS_16 = 57600;
    private static final double HOURS_12 = 43200;
    private static final double HOURS_8  = 28800;
    private static final double HOURS_4  = 14400;
    private static final double HOURS_2  = 7200;

    public static StatsFeedback findFeedback(double avgSmokeCount, double avgPatienceTimeSeconds) {
        if (avgSmokeCount <= 0) return PERFECT;

        if (avgSmokeCount <= 3) {
            if (avgPatienceTimeSeconds > HOURS_20) return EXCELLENT;
            if (avgPatienceTimeSeconds > HOURS_16) return GOOD_TIME;
            if (avgPatienceTimeSeconds > HOURS_12) return STEADY;
            return MORNING;
        }

        if (avgSmokeCount <= 5) {
            if (avgPatienceTimeSeconds > HOURS_8) return QUITE_GOOD;
            if (avgPatienceTimeSeconds > HOURS_4) return START_GOOD;
            return SHORT_INTERVAL;
        }

        if (avgSmokeCount <= 10) {
            if (avgPatienceTimeSeconds > HOURS_4) return NOT_BAD;
            if (avgPatienceTimeSeconds > HOURS_2) return TOO_SHORT;
            return VERY_SHORT;
        }

        if (avgSmokeCount <= 15) {
            if (avgPatienceTimeSeconds > HOURS_2) return HIGH_COUNT;
            return WARNING;
        }

        if (avgPatienceTimeSeconds > HOURS_2) return DANGER;
        return EXTREME;
    }
}
