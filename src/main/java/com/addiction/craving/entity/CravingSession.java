package com.addiction.craving.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.global.exception.AddictionException;
import com.addiction.user.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "craving_session",
        indexes = {
                @Index(name = "idx_craving_session_user_started_at", columnList = "user_id,started_at")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CravingSession extends BaseTimeEntity {
    public static final int HOLD_DURATION_SECONDS = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CravingSessionStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public static CravingSession start(User user, LocalDateTime startedAt) {
        CravingSession session = new CravingSession();
        session.user = user;
        session.status = CravingSessionStatus.ACTIVE;
        session.startedAt = startedAt;
        return session;
    }

    public void complete(LocalDateTime completedAt) {
        if (status == CravingSessionStatus.COMPLETED) {
            return;
        }

        long elapsedSeconds = Duration.between(startedAt, completedAt).getSeconds();
        if (elapsedSeconds < HOLD_DURATION_SECONDS) {
            throw new AddictionException("30초를 버틴 뒤 완료할 수 있습니다.");
        }

        status = CravingSessionStatus.COMPLETED;
        this.completedAt = completedAt;
    }

    public boolean belongsTo(Long userId) {
        return user.getId().equals(userId);
    }
}
