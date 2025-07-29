package com.addiction.inquiry.inquryQuestion.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;

    private String question;

    @Builder
    public InquiryQuestion(int id, String question, String title, User user) {
        this.id = id;
        this.question = question;
        this.title = title;
        this.user = user;
    }
}