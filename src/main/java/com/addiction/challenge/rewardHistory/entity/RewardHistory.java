package com.addiction.challenge.rewardHistory.entity;

import com.addiction.common.enums.RewardType;
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
public class RewardHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private RewardType type;

    private Integer point;

    @Column(name = "remaining_point")
    private Integer remainingPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Builder
    public RewardHistory(Long id, RewardType type, Integer point, Integer remainingPoint, User userId) {
        this.id = id;
        this.type = type;
        this.point = point;
        this.remainingPoint = remainingPoint;
        this.userId = userId;
    }
}
