package com.addiction.survey.surveyQuestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;

public interface SurveyQuestionJpaRepository extends JpaRepository<SurveyQuestion, Integer> {


}
