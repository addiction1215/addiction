package com.addiction.survey.userSurveyAnswer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.survey.userSurveyAnswer.entity.UserSurveyAnswer;

public interface UserSurveyAnswerJpaRepository extends JpaRepository<UserSurveyAnswer, Long> {
}
