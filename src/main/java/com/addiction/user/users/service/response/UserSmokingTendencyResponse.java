package com.addiction.user.users.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSmokingTendencyResponse {

    @Schema(description = "설문 이력 존재 여부. false이면 score, level, scoreChange는 null입니다.", example = "true")
    private final boolean hasSurvey;
    @Schema(description = "원점수 21~99를 0~100점으로 역정규화한 QuitMate 점수", example = "59", minimum = "0", maximum = "100", nullable = true)
    private final Integer quitMateScore;
    @Schema(description = "흡연 성향 단계. SEVERE: 0~39점, MODERATE: 40~59점, MILD: 60~100점", example = "MODERATE", allowableValues = {"SEVERE", "MODERATE", "MILD"}, nullable = true)
    private final SmokingTendencyLevel level;
    @Schema(description = "직전 설문 대비 상태. 설문 이력이 1회 이하이면 NOT_AVAILABLE입니다.", example = "SCORE_INCREASED", allowableValues = {"LEVEL_IMPROVED", "SCORE_INCREASED", "UNCHANGED", "SCORE_DECREASED", "LEVEL_WORSENED", "NOT_AVAILABLE"})
    private final SmokingTendencyComparisonStatus comparisonStatus;
    @Schema(description = "현재 QuitMate 점수 - 직전 QuitMate 점수. 비교할 이전 설문이 없으면 null입니다.", example = "9", nullable = true)
    private final Integer scoreChange;

    @Builder
    public UserSmokingTendencyResponse(
            boolean hasSurvey,
            Integer quitMateScore,
            SmokingTendencyLevel level,
            SmokingTendencyComparisonStatus comparisonStatus,
            Integer scoreChange
    ) {
        this.hasSurvey = hasSurvey;
        this.quitMateScore = quitMateScore;
        this.level = level;
        this.comparisonStatus = comparisonStatus;
        this.scoreChange = scoreChange;
    }

    public static UserSmokingTendencyResponse noSurvey() {
        return UserSmokingTendencyResponse.builder()
                .hasSurvey(false)
                .comparisonStatus(SmokingTendencyComparisonStatus.NOT_AVAILABLE)
                .build();
    }
}
