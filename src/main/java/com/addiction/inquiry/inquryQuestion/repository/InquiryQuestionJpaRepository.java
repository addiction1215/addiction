package com.addiction.inquiry.inquryQuestion.repository;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryQuestionJpaRepository extends JpaRepository<InquiryQuestion, Integer> {
}
