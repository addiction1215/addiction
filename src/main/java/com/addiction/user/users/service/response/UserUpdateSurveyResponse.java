package com.addiction.user.users.service.response;

import com.addiction.survey.surveyResult.entity.SurveyResult;
import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserUpdateSurveyResponse {
    private final String resultTitle;
    private final String resultStatus;
    private final int score;
    private final List<String> result;

    @Builder
    public UserUpdateSurveyResponse(String resultTitle, String resultStatus, int score, List<String> result) {
        this.resultTitle = resultTitle;
        this.resultStatus = resultStatus;
        this.score = score;
        this.result = result;
    }

    public static UserUpdateSurveyResponse of(SurveyResult surveyResult, int score) {
        return UserUpdateSurveyResponse.builder()
                .resultTitle(surveyResult.getTitle())
                .resultStatus(surveyResult.getStatus())
                .score(score)
                .result(
                        surveyResult.getDescriptions().stream().map(SurveyResultDescription::getDescription).toList()
                )
                .build();
    }
}
