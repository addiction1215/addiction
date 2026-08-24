package com.addiction.pushOutbox.entity;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.dailySmokingPush.entity.DailySmokingPushSchedule;
import com.addiction.global.BaseTimeEntity;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "push_outbox",
        uniqueConstraints = @UniqueConstraint(name = "uk_push_outbox_schedule_delivery_date", columnNames = {"schedule_id", "delivery_date"}),
        indexes = @Index(name = "idx_push_outbox_dispatch", columnList = "status,next_attempt_at"))
public class PushOutbox extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private DailySmokingPushSchedule schedule;

    @Column(nullable = false)
    private LocalDate deliveryDate;

    @Column(nullable = false, length = 500)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AlertDestinationType destinationType;

    @Column(nullable = false, length = 100)
    private String destinationInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PushOutboxStatus status;

    @Column(nullable = false)
    private int attemptCount;

    @Column(nullable = false)
    private LocalDateTime nextAttemptAt;

    private LocalDateTime processingStartedAt;
    private LocalDateTime sentAt;

    @Column(length = 1000)
    private String lastError;

    public void markSent(LocalDateTime now) {
        this.status = PushOutboxStatus.SENT;
        this.sentAt = now;
        this.processingStartedAt = null;
        this.lastError = null;
    }

    public void retryAt(LocalDateTime nextAttemptAt, String errorMessage) {
        this.status = PushOutboxStatus.RETRY;
        this.nextAttemptAt = nextAttemptAt;
        this.processingStartedAt = null;
        this.lastError = abbreviate(errorMessage);
    }

    public void markFailed(String errorMessage) {
        this.status = PushOutboxStatus.FAILED;
        this.processingStartedAt = null;
        this.lastError = abbreviate(errorMessage);
    }

    private String abbreviate(String message) {
        if (message == null) return null;
        return message.length() <= 1000 ? message : message.substring(0, 1000);
    }
}
