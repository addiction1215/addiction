package com.addiction.user.userCigaretteHistory.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmokingFeedback {

    COMPLETE_QUIT(
            0,
            -100.1, 0.1,   // -100.0 (평소 피웠는데 어제 0) 또는 0.0 (계속 0) 포함
            -100.1, 0.1,   // -100.0 (그제 피웠는데 어제 0) 또는 0.0 (계속 0) 포함
            "어제 단 한 개비도 안 피셨군요! 금연 완성 단계입니다. 작은 보상 습관으로 유지하세요."
    ),

    MAJOR_ACHIEVEMENT_1(
            1,
            -90.0, -50.0,
            -90.0, -50.0,
            "큰 성과에요! 지금 흐름을 이어가면 금연이 훨씬 가까워져요."
    ),

    STEADY_REDUCTION_1(
            2,
            -90.0, -50.0,
            -50.0, -25.0,
            "꾸준히 줄이고 있어요. 스스로를 믿고 계속 이어가 보세요."
    ),

    MUCH_REDUCED_1(
            3,
            -90.0, -50.0,
            -25.0, 25.0,
            "평소보다 많이 줄였어요. 안정적으로 습관으로 굳혀가면 좋아요."
    ),

    REDUCED_BUT_INCREASED_1(
            4,
            -90.0, -50.0,
            25.0, 50.0,
            "평소 대비해선 줄였지만, 어제는 그제보다 조금 늘었네요. 다시 균형을 잡아가면 됩니다."
    ),

    REDUCED_BUT_MUCH_INCREASED_1(
            5,
            -90.0, -50.0,
            50.0, Double.MAX_VALUE,
            "평소보다 줄었지만 어제는 그제보다 많이 흡연을 하셨네요. 큰 흐름은 좋으니, 단기 흔들림 원인을 찾아서 관리해보아요."
    ),

    NOTICEABLE_REDUCTION(
            6,
            -50.0, -25.0,
            -90.0, -50.0,
            "눈에 띄게 줄였어요! 이대로 가면 금연 성공 확률이 크게 올라갑니다."
    ),

    STEADY_REDUCTION_2(
            7,
            -50.0, -25.0,
            -50.0, -25.0,
            "꾸준히 줄어드는 중이에요. 한 걸음씩 잘 나아가고 있어요."
    ),

    GRADUALLY_REDUCING(
            8,
            -50.0, -25.0,
            -25.0, 25.0,
            "조금씩 줄어들고 있어요. 지금 흐름을 안정적으로 유지해 보세요."
    ),

    REDUCED_BUT_INCREASED_2(
            9,
            -50.0, -25.0,
            25.0, 50.0,
            "평소보다 줄었는데 어제는 늘었어요. 흔들려도 괜찮아요, 다시 조절해 보세요."
    ),

    REDUCED_BUT_MUCH_INCREASED_2(
            10,
            -50.0, -25.0,
            50.0, Double.MAX_VALUE,
            "평소 대비 흡연량이 줄었는데, 어제 많이 늘었어요. 다시 리듬을 회복할 수 있어요."
    ),

    NO_CHANGE_BUT_REDUCED(
            11,
            -25.0, 25.0,
            -90.0, -50.0,
            "큰 변화는 없지만 어제는 확 줄였네요. 잘하셨어요!"
    ),

    SIMILAR_BUT_REDUCED(
            12,
            -25.0, 25.0,
            -50.0, -25.0,
            "평소와 비슷하지만 어제는 줄었어요. 작은 성취도 칭찬할 만 해요."
    ),

    NO_CHANGE(
            13,
            -25.0, 25.0,
            -25.0, 25.0,
            "큰 변화는 없네요. 새로운 목표나 동기를 찾아 보는 건 어떨까요?"
    ),

    SIMILAR_BUT_INCREASED(
            14,
            -25.0, 25.0,
            25.0, 50.0,
            "평소와 비슷하지만 어제는 조금 늘었어요. 원인을 찾아서 조절해보세요."
    ),

    NO_CHANGE_BUT_MUCH_INCREASED(
            15,
            -25.0, 25.0,
            50.0, Double.MAX_VALUE,
            "큰 변화는 없지만 어제 엄청 많이 늘었어요. 방심하지 않도록 주의해 보세요."
    ),

    INCREASED_BUT_REDUCED_1(
            16,
            25.0, 50.0,
            -90.0, -50.0,
            "평소 대비 좀 늘었지만 어제는 줄었어요. 반전의 시작일 수 있어요."
    ),

    INCREASED_BUT_REDUCED_2(
            17,
            25.0, 50.0,
            -50.0, -25.0,
            "늘었다가 줄었네요. 좋은 신호예요, 이어가 보세요."
    ),

    INCREASED_STATE(
            18,
            25.0, 50.0,
            -25.0, 25.0,
            "평소보다 늘어난 상태예요. 지금이 조절을 시작할 때입니다."
    ),

    INCREASED_BOTH(
            19,
            25.0, 50.0,
            25.0, 50.0,
            "평소보다도, 어제도 늘었어요. 위험 신호, 조금 더 관리가 필요해요."
    ),

    MUCH_INCREASED_1(
            20,
            25.0, 50.0,
            50.0, Double.MAX_VALUE,
            "평소보다도 늘었지만, 어제는 크게 늘었어요. 지금 바로 조절이 필요해요."
    ),

    MUCH_INCREASED_BUT_REDUCED_1(
            21,
            50.0, Double.MAX_VALUE,
            -90.0, -50.0,
            "많이 늘었지만, 어제는 확 줄였네요. 금연, 이제부터 시작입니다!"
    ),

    MUCH_INCREASED_BUT_REDUCED_2(
            22,
            50.0, Double.MAX_VALUE,
            -50.0, -25.0,
            "평소 대비해선 늘었지만, 어제 줄였어요. 흐름을 이어가면 됩니다."
    ),

    SIGNIFICANTLY_INCREASED_STATE(
            23,
            50.0, Double.MAX_VALUE,
            -25.0, 25.0,
            "흡연량이 크게 늘어난 상태예요. 지금 바로 대책을 세워야 해요."
    ),

    CONTINUOUSLY_INCREASING(
            24,
            50.0, Double.MAX_VALUE,
            25.0, 50.0,
            "계속 늘어나는 추세예요. 적극적인 관리가 꼭 필요합니다."
    ),

    EXPLOSIVELY_INCREASED(
            25,
            50.0, Double.MAX_VALUE,
            50.0, Double.MAX_VALUE,
            "흡연량이 폭발적으로 늘었어요. 지금은 전문가 도움도 고려해 보세요."
    );

    private final int conditionNo;
    private final double usualCompareMin;
    private final double usualCompareMax;
    private final double dayBeforeCompareMin;
    private final double dayBeforeCompareMax;
    private final String message;

    public static SmokingFeedback findFeedback(double usualChangeRate, double dayBeforeChangeRate) {
        for (SmokingFeedback feedback : values()) {
            if (isInRange(usualChangeRate, feedback.usualCompareMin, feedback.usualCompareMax) &&
                    isInRange(dayBeforeChangeRate, feedback.dayBeforeCompareMin, feedback.dayBeforeCompareMax)) {
                return feedback;
            }
        }
        return NO_CHANGE; // 기본값
    }

    private static boolean isInRange(double value, double min, double max) {
        return value >= min && value < max;
    }
}
