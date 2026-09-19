package com.addiction.user.users.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSmokingTendencyResponse {

    private final boolean hasSurvey;
    private final Integer rawScore;
    private final Integer quitMateScore;
    private final SmokingTendencyLevel level;

    @Builder
    public UserSmokingTendencyResponse(
            boolean hasSurvey,
            Integer rawScore,
            Integer quitMateScore,
            SmokingTendencyLevel level
    ) {
        this.hasSurvey = hasSurvey;
        this.rawScore = rawScore;
        this.quitMateScore = quitMateScore;
        this.level = level;
    }

    public static UserSmokingTendencyResponse noSurvey() {
        return UserSmokingTendencyResponse.builder()
                .hasSurvey(false)
                .build();
    }
}
