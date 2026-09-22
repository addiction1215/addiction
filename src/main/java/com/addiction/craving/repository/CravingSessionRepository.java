package com.addiction.craving.repository;

import com.addiction.craving.entity.CravingSession;
import com.addiction.craving.entity.CravingSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CravingSessionRepository extends JpaRepository<CravingSession, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select session from CravingSession session where session.id = :id")
    Optional<CravingSession> findWithLockById(@Param("id") Long id);

    long countByUser_IdAndStatusAndCompletedAtBetween(
            Long userId,
            CravingSessionStatus status,
            LocalDateTime dayStart,
            LocalDateTime nextDayStart
    );
}
