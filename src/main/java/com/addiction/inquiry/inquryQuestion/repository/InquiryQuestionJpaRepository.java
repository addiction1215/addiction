package com.addiction.inquiry.inquryQuestion.repository;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.user.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryQuestionJpaRepository extends JpaRepository<InquiryQuestion, Integer> {

    List<InquiryQuestion> findAllByUserIdAndInquiryStatus(int userId, InquiryStatus inquiryStatus);

    int user(User user);
}
