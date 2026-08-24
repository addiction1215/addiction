package com.addiction.dailySmokingPush.entity;

import com.addiction.common.enums.DailySmokingFeedbackTime;
import com.addiction.global.BaseTimeEntity;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_smoking_push_schedule",
        uniqueConstraints = @UniqueConstraint(name = "uk_daily_smoking_push_schedule_user_slot", columnNames = {"user_id", "slot"}),
        indexes = @Index(name = "idx_daily_smoking_push_schedule_due", columnList = "enabled,send_time"))
public class DailySmokingPushSchedule extends BaseTimeEntity {

    private static final String KOREA_TIMEZONE = "Asia/Seoul";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DailySmokingFeedbackTime slot;

    @Column(name = "send_time", nullable = false)
    private LocalTime sendTime;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false, length = 50)
    private String timezone;

    private DailySmokingPushSchedule(User user, DailySmokingFeedbackTime slot, LocalTime sendTime) {
        this.user = user;
        this.slot = slot;
        this.sendTime = sendTime;
        this.enabled = true;
        this.timezone = KOREA_TIMEZONE;
    }

    public static List<DailySmokingPushSchedule> createDefaults(User user) {
        return List.of(
                new DailySmokingPushSchedule(user, DailySmokingFeedbackTime.MORNING, LocalTime.of(8, 45)),
                new DailySmokingPushSchedule(user, DailySmokingFeedbackTime.LUNCH, LocalTime.of(12, 30)),
                new DailySmokingPushSchedule(user, DailySmokingFeedbackTime.DINNER, LocalTime.of(18, 30)),
                new DailySmokingPushSchedule(user, DailySmokingFeedbackTime.NIGHT, LocalTime.of(21, 0))
        );
    }

    public void update(LocalTime sendTime, boolean enabled) {
        this.sendTime = sendTime;
        this.enabled = enabled;
    }
}
