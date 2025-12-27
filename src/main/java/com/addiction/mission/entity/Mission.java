package com.addiction.mission.entity;

import com.addiction.challenge.entity.Challenge;
import com.addiction.common.enums.MissionCategoryStatus;
import com.addiction.global.BaseTimeEntity;
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
    private Challenge challenge;

    private MissionCategoryStatus category;

    private String title;

    private Integer reward;

    private String content;

    @Builder
    public Mission(Long id, Challenge challenge, MissionCategoryStatus category, String title, Integer reward, String content) {
        this.id = id;
        this.challenge = challenge;
        this.category = category;
        this.title = title;
        this.reward = reward;
        this.content = content;
    }
}
