package com.addiction.user.users.service.response;

import com.addiction.survey.surveyResult.entity.SurveyResult;
import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class UserUpdateSurveyResponse {
    @Schema(description = "설문 결과 제목", example = "중등도 흡연자")
    private final String resultTitle;
    @Schema(description = "설문 결과 상태", example = "중등도")
    private final String resultStatus;
    @Schema(description = "선택한 설문 답변으로 계산한 원점수", example = "53")
    private final int score;
    @ArraySchema(schema = @Schema(description = "설문 결과 설명", example = "금연 계획을 세워보세요."))
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
                        surveyResult.getDescriptions() == null
                                ? Collections.emptyList()
                                : surveyResult.getDescriptions().stream()
                                        .map(SurveyResultDescription::getDescription)
                                        .toList()
                )
                .build();
    }
}
