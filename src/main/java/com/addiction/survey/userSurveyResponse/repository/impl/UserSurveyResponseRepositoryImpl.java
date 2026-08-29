package com.addiction.survey.userSurveyResponse.repository.impl;

import org.springframework.stereotype.Repository;

import com.addiction.survey.userSurveyResponse.entity.UserSurveyResponse;
import com.addiction.survey.userSurveyResponse.repository.UserSurveyResponseJpaRepository;
import com.addiction.survey.userSurveyResponse.repository.UserSurveyResponseRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserSurveyResponseRepositoryImpl implements UserSurveyResponseRepository {
    private final UserSurveyResponseJpaRepository userSurveyResponseJpaRepository;

    @Override
    public UserSurveyResponse save(UserSurveyResponse userSurveyResponse) {
        return userSurveyResponseJpaRepository.save(userSurveyResponse);
    }
}
