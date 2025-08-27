package com.addiction.survey.surveyQuestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;

import java.util.List;

public interface SurveyQuestionJpaRepository extends JpaRepository<SurveyQuestion, Long> {

    List<SurveyQuestion> findAllByOrderBySortAsc();

}
