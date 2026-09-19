package com.addiction.user.users.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSmokingTendencyResponse {

    private final boolean hasSurvey;
    private final Integer quitMateScore;
    private final SmokingTendencyLevel level;
    private final SmokingTendencyComparisonStatus comparisonStatus;

    @Builder
    public UserSmokingTendencyResponse(
            boolean hasSurvey,
            Integer quitMateScore,
            SmokingTendencyLevel level,
            SmokingTendencyComparisonStatus comparisonStatus
    ) {
        this.hasSurvey = hasSurvey;
        this.quitMateScore = quitMateScore;
        this.level = level;
        this.comparisonStatus = comparisonStatus;
    }

    public static UserSmokingTendencyResponse noSurvey() {
        return UserSmokingTendencyResponse.builder()
                .hasSurvey(false)
                .comparisonStatus(SmokingTendencyComparisonStatus.NOT_AVAILABLE)
                .build();
    }
}
