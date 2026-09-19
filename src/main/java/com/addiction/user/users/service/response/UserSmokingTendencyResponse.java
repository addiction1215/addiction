package com.addiction.user.users.service.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserSmokingTendencyResponse {

    private final boolean hasSurvey;
    private final boolean hasComparison;
    private final Integer rawScore;
    private final Integer quitMateScore;
    private final SmokingTendencyLevel level;
    private final Integer previousQuitMateScore;
    private final SmokingTendencyComparisonStatus comparisonStatus;
    private final Integer scoreChange;
    private final Integer levelChange;
    private final LocalDateTime lastSurveyedAt;

    @Builder
    public UserSmokingTendencyResponse(
            boolean hasSurvey,
            boolean hasComparison,
            Integer rawScore,
            Integer quitMateScore,
            SmokingTendencyLevel level,
            Integer previousQuitMateScore,
            SmokingTendencyComparisonStatus comparisonStatus,
            Integer scoreChange,
            Integer levelChange,
            LocalDateTime lastSurveyedAt
    ) {
        this.hasSurvey = hasSurvey;
        this.hasComparison = hasComparison;
        this.rawScore = rawScore;
        this.quitMateScore = quitMateScore;
        this.level = level;
        this.previousQuitMateScore = previousQuitMateScore;
        this.comparisonStatus = comparisonStatus;
        this.scoreChange = scoreChange;
        this.levelChange = levelChange;
        this.lastSurveyedAt = lastSurveyedAt;
    }

    public static UserSmokingTendencyResponse noSurvey() {
        return UserSmokingTendencyResponse.builder()
                .hasSurvey(false)
                .hasComparison(false)
                .comparisonStatus(SmokingTendencyComparisonStatus.NOT_AVAILABLE)
                .build();
    }
}
