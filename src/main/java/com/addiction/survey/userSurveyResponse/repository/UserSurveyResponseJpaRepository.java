package com.addiction.survey.userSurveyResponse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.survey.userSurveyResponse.entity.UserSurveyResponse;

import java.util.List;

public interface UserSurveyResponseJpaRepository extends JpaRepository<UserSurveyResponse, Long> {
    List<UserSurveyResponse> findTop2ByUserIdOrderBySubmittedAtDescIdDesc(Long userId);
}
