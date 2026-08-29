package com.addiction.survey.userSurveyAnswer.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.userSurveyResponse.entity.UserSurveyResponse;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
// JPA가 엔티티를 조회할 때 사용할 기본 생성자를 만들되, 외부 직접 생성은 막는다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_survey_answer")
public class UserSurveyAnswer extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_survey_response_id", nullable = false)
    private UserSurveyResponse response;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_question_id", nullable = false)
    private SurveyQuestion surveyQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_answer_id")
    private SurveyAnswer surveyAnswer;

    private Integer numericValue;

    private UserSurveyAnswer(UserSurveyResponse response, SurveyAnswer surveyAnswer) {
        this.response = response;
        this.surveyQuestion = surveyAnswer.getSurveyQuestion();
        this.surveyAnswer = surveyAnswer;
    }

    private UserSurveyAnswer(UserSurveyResponse response, SurveyQuestion surveyQuestion, Integer numericValue) {
        this.response = response;
        this.surveyQuestion = surveyQuestion;
        this.numericValue = numericValue;
    }

    public static UserSurveyAnswer create(UserSurveyResponse response, SurveyAnswer surveyAnswer) {
        return new UserSurveyAnswer(response, surveyAnswer);
    }

    public static UserSurveyAnswer createNumeric(UserSurveyResponse response, SurveyQuestion surveyQuestion, Integer numericValue) {
        return new UserSurveyAnswer(response, surveyQuestion, numericValue);
    }
}
