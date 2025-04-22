package com.addiction.survey.surveyAnswer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;

public interface SurveyAnswerJpaRepository extends JpaRepository<SurveyAnswer, Integer> {


}
