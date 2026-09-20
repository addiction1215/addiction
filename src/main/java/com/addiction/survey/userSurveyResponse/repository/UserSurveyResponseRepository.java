package com.addiction.survey.userSurveyResponse.repository;

import com.addiction.survey.userSurveyResponse.entity.UserSurveyResponse;

import java.util.List;

public interface UserSurveyResponseRepository {
    UserSurveyResponse save(UserSurveyResponse userSurveyResponse);

    List<UserSurveyResponse> findLatestTwoByUserId(Long userId);
}
