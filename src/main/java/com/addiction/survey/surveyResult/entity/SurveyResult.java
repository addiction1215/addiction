package com.addiction.survey.surveyResult.entity;

import java.util.ArrayList;
import java.util.List;

import com.addiction.global.BaseTimeEntity;
import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyResult extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String title;

	private int score;

	@OneToMany(mappedBy = "surveyResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SurveyResultDescription> descriptions = new ArrayList<>();

	@Builder
	public SurveyResult(int id, String title, int score, List<SurveyResultDescription> descriptions) {
		this.id = id;
		this.title = title;
		this.score = score;
		this.descriptions = descriptions;
	}
}
