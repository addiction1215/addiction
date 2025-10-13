package com.addiction.challenge.mission.entity;

import com.addiction.challenge.challenge.entity.Challenge;
import com.addiction.common.enums.MissionCategoryStatus;
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
public class Mission extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challengeId;

    private MissionCategoryStatus category;

    private String title;

    private Integer reward;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Builder
    public Mission(Long id, Challenge challengeId, MissionCategoryStatus category, String title, Integer reward, String content, User userId) {
        this.id = id;
        this.challengeId = challengeId;
        this.category = category;
        this.title = title;
        this.reward = reward;
        this.content = content;
        this.userId = userId;
    }
}
