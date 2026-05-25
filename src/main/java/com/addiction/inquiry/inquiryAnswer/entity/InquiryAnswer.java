package com.addiction.inquiry.inquiryAnswer.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inquiry_answer")
public class InquiryAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_question_id", nullable = false)
    private InquiryQuestion inquiryQuestion;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Builder
    public InquiryAnswer(Long id, InquiryQuestion inquiryQuestion, String content) {
        this.id = id;
        this.inquiryQuestion = inquiryQuestion;
        this.content = content;
    }
}
