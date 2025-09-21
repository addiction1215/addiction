package com.addiction.survey.surveyResultDescription.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;

public interface SurveyResultDescriptionJpaRepository extends JpaRepository<SurveyResultDescription, Long> {
}
