package com.addiction.challenge.challenge.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Challenge extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String badge;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Challenge(Long id, String title, String content, String badge, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.user = user;
    }
}
