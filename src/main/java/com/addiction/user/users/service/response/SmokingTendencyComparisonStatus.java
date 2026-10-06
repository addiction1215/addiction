package com.addiction.user.users.service.response;

import io.swagger.v3.oas.annotations.media.Schema;

public enum SmokingTendencyComparisonStatus {
    @Schema(description = "흡연 성향 단계가 좋아짐")
    LEVEL_IMPROVED,
    @Schema(description = "단계는 같지만 QuitMate 점수가 상승")
    SCORE_INCREASED,
    @Schema(description = "단계와 QuitMate 점수가 모두 동일")
    UNCHANGED,
    @Schema(description = "단계는 같지만 QuitMate 점수가 하락")
    SCORE_DECREASED,
    @Schema(description = "흡연 성향 단계가 나빠짐")
    LEVEL_WORSENED,
    @Schema(description = "비교할 이전 설문 이력이 없음")
    NOT_AVAILABLE
}
