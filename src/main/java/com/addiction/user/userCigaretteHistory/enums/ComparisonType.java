package com.addiction.user.userCigaretteHistory.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 주간 비교 타입
 * COUNT: 흡연 횟수 비교
 * TIME: 금연 시간 비교
 */
@Getter
@RequiredArgsConstructor
public enum ComparisonType {
    COUNT("횟수"),
    TIME("시간");

    private final String description;
}
