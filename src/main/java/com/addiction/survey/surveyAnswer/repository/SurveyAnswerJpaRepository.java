package com.addiction.survey.surveyAnswer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.addiction.survey.surveyAnswer.entity.SurveyAnswer;

public interface SurveyAnswerJpaRepository extends JpaRepository<SurveyAnswer, Long> {


    /**
     * 설문 제출 시 전달받은 선택지 ID 목록에 해당하는 답변과, 각 답변이 속한 질문을 함께 조회한다.
     * <p>
     * 이후 서비스에서 답변이 어느 질문에 속하는지 검증하고 총점을 계산하므로,
     * {@code join fetch}로 {@code surveyQuestion}을 즉시 조회해 N+1 쿼리를 방지한다.
     */
    @Query("select surveyAnswer from SurveyAnswer surveyAnswer join fetch surveyAnswer.surveyQuestion where surveyAnswer.id in :ids")
    List<SurveyAnswer> findAllByIdInWithSurveyQuestion(@Param("ids") List<Long> ids);

}
