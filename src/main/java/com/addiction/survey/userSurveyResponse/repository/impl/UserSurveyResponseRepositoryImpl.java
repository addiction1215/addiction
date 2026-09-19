package com.addiction.survey.userSurveyResponse.repository.impl;

import org.springframework.stereotype.Repository;

import com.addiction.survey.userSurveyResponse.entity.UserSurveyResponse;
import com.addiction.survey.userSurveyResponse.repository.UserSurveyResponseJpaRepository;
import com.addiction.survey.userSurveyResponse.repository.UserSurveyResponseRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserSurveyResponseRepositoryImpl implements UserSurveyResponseRepository {
    private final UserSurveyResponseJpaRepository userSurveyResponseJpaRepository;

    @Override
    public UserSurveyResponse save(UserSurveyResponse userSurveyResponse) {
        return userSurveyResponseJpaRepository.save(userSurveyResponse);
    }

    @Override
    public Optional<UserSurveyResponse> findLatestByUserId(Long userId) {
        return userSurveyResponseJpaRepository.findTopByUserIdOrderBySubmittedAtDescIdDesc(userId);
    }
}
