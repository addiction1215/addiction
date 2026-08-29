package com.addiction.survey.surveyQuestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addiction.survey.surveyQuestion.entity.SurveyQuestion;
import com.addiction.survey.surveyQuestion.enums.SurveyType;

import java.util.List;

public interface SurveyQuestionJpaRepository extends JpaRepository<SurveyQuestion, Long> {

    List<SurveyQuestion> findAllByOrderBySortAsc();

    /**
     * 지정한 설문 유형의 질문을 {@code sort} 오름차순으로 조회한다.
     * <p>
     * Spring Data JPA가 메서드 이름을 해석해 아래와 동등한 쿼리를 생성한다.
     * {@code SELECT * FROM survey_question WHERE survey_type = ? ORDER BY sort ASC}
     */
    List<SurveyQuestion> findAllBySurveyTypeOrderBySortAsc(SurveyType surveyType);

}
