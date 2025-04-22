package com.addiction.survey.surveyResult.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.addiction.survey.surveyResult.entity.SurveyResult;

public interface SurveyResultJpaRepository extends JpaRepository<SurveyResult, Integer> {
	@Query("SELECT sr FROM SurveyResult sr WHERE sr.score <= :score ORDER BY sr.score DESC LIMIT 1")
	Optional<SurveyResult> findClosestScore(@Param("score") int score);
}
