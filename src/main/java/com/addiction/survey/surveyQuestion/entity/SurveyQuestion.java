package com.addiction.survey.surveyQuestion.entity;

import java.util.ArrayList;
import java.util.List;

import com.addiction.global.BaseTimeEntity;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyQuestion.enums.SurveyType;
import com.addiction.user.refreshToken.entity.RefreshToken;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class SurveyQuestion extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String question;

	@Enumerated(EnumType.STRING)
	private SurveyType surveyType;

	@OneToMany(mappedBy = "surveyQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SurveyAnswer> surveyAnswers = new ArrayList<>();

	@Builder
	public SurveyQuestion(String question, SurveyType surveyType) {
		this.question = question;
		this.surveyType = surveyType;
	}
}
