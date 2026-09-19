package com.addiction.survey.userSurveyResponse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.survey.userSurveyResponse.entity.UserSurveyResponse;

import java.util.Optional;

public interface UserSurveyResponseJpaRepository extends JpaRepository<UserSurveyResponse, Long> {
    Optional<UserSurveyResponse> findTopByUserIdOrderBySubmittedAtDescIdDesc(Long userId);
}
