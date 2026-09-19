package com.addiction.survey.userSurveyResponse.repository;

import com.addiction.survey.userSurveyResponse.entity.UserSurveyResponse;

import java.util.Optional;

public interface UserSurveyResponseRepository {
    UserSurveyResponse save(UserSurveyResponse userSurveyResponse);

    Optional<UserSurveyResponse> findLatestByUserId(Long userId);
}
