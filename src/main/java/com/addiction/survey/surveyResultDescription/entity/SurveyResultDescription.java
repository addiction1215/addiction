package com.addiction.survey.surveyResultDescription.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.survey.surveyResult.entity.SurveyResult;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyResultDescription extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private SurveyResult surveyResult;

	private String description;

	@Builder
	public SurveyResultDescription(Long id, SurveyResult surveyResult, String description) {
		this.id = id;
		this.surveyResult = surveyResult;
		this.description = description;
	}
}
