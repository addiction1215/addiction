package com.addiction.inquiry.inquryQuestion.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.inquiry.inquryQuestion.enums.InquiryStatus;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;

    private String question;

    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus;

    @ElementCollection
    @CollectionTable(name = "inquiry_question_image_keys", joinColumns = @JoinColumn(name = "inquiry_question_id"))
    @Column(name = "image_key")
    private List<String> imageKeys;

    @Builder
    public InquiryQuestion(Long id, String question, String title, User user, InquiryStatus inquiryStatus, List<String> imageKeys) {
        this.id = id;
        this.question = question;
        this.title = title;
        this.user = user;
        this.inquiryStatus = inquiryStatus;
        this.imageKeys = imageKeys;
    }

}