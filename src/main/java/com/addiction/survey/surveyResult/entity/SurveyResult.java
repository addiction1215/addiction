package com.addiction.survey.surveyResult.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.survey.surveyResultDescription.entity.SurveyResultDescription;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyResult extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Integer score;

    private String status;

    @OneToMany(mappedBy = "surveyResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SurveyResultDescription> descriptions = new ArrayList<>();

    @Builder
    public SurveyResult(Long id, String title, Integer score, String status, List<SurveyResultDescription> descriptions) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.status = status;
        this.descriptions = descriptions;
    }
}
