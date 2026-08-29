package com.addiction.survey.userSurveyResponse.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.addiction.global.BaseTimeEntity;
import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;
import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.userSurveyAnswer.entity.UserSurveyAnswer;
import com.addiction.user.users.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_survey_response")
public class UserSurveyResponse extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer totalScore;
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<UserSurveyAnswer> answers = new ArrayList<>();

    private UserSurveyResponse(User user, int totalScore) {
        this.user = user;
        this.totalScore = totalScore;
        this.submittedAt = LocalDateTime.now();
    }

    public static UserSurveyResponse create(User user, int totalScore) {
        return new UserSurveyResponse(user, totalScore);
    }

    public void addAnswer(SurveyAnswer surveyAnswer) {
        answers.add(UserSurveyAnswer.create(this, surveyAnswer));
    }

    public void addNumericAnswer(SurveyQuestion surveyQuestion, Integer numericValue) {
        answers.add(UserSurveyAnswer.createNumeric(this, surveyQuestion, numericValue));
    }
}
