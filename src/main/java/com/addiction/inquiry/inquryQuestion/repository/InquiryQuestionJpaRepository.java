package com.addiction.inquiry.inquryQuestion.repository;

import com.addiction.inquiry.inquryQuestion.entity.InquiryQuestion;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.user.users.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InquiryQuestionJpaRepository extends JpaRepository<InquiryQuestion, Long> {

    List<InquiryQuestion> findAllByUserIdAndInquiryStatus(Long userId, InquiryStatus inquiryStatus);

    @Query("""
            select distinct inquiryQuestion
            from InquiryQuestion inquiryQuestion
            left join fetch inquiryQuestion.inquiryAnswers inquiryAnswers
            where inquiryQuestion.id = :id
            """)
    Optional<InquiryQuestion> findDetailById(Long id);

    int user(User user);
}
