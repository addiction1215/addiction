package com.addiction.pushOutbox.repository;

import com.addiction.pushOutbox.entity.PushOutbox;
import com.addiction.pushOutbox.entity.PushOutboxStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface PushOutboxJpaRepository extends JpaRepository<PushOutbox, Long> {

    /**
     * 일일 피드백 발송 대기 건을 생성한다.
     *
     * <p>{@code uk_push_outbox_schedule_delivery_date} 유니크 제약과 MySQL의 {@code INSERT IGNORE}를 이용해,
     * 배치가 재실행되거나 여러 서버가 동시에 실행돼도 같은 슬롯의 같은 날짜 발송 건은 한 번만 생성한다.</p>
     *
     * @return 새 Outbox 행을 생성하면 1, 이미 같은 발송 건이 있으면 0
     */
    @Modifying
    @Query(value = """
            insert ignore into push_outbox (
                user_id, schedule_id, delivery_date, body, destination_type, destination_info,
                status, attempt_count, next_attempt_at, created_date, updated_date
            ) values (
                :userId, :scheduleId, :deliveryDate, :body, :destinationType, :destinationInfo,
                'PENDING', 0, :now, :now, :now
            )
            """, nativeQuery = true)
    int insertPendingIfAbsent(@Param("userId") Long userId,
                              @Param("scheduleId") Long scheduleId,
                              @Param("deliveryDate") LocalDate deliveryDate,
                              @Param("body") String body,
                              @Param("destinationType") String destinationType,
                              @Param("destinationInfo") String destinationInfo,
                              @Param("now") LocalDateTime now);

    /**
     * 지금 발송 또는 재시도가 가능한 Outbox 후보를 생성일 순으로 조회한다.
     *
     * <p>발송 워커가 사용자 Push 토큰을 바로 사용할 수 있도록 {@code user}와 {@code user.pushes}를 함께 조회한다.
     * 실제 발송 권한은 이 조회만으로 확보되지 않으므로, 워커는 이어서 {@link #claim}으로 원자적 선점을 시도한다.</p>
     */
    @EntityGraph(attributePaths = {"user", "user.pushes"})
    List<PushOutbox> findByStatusInAndNextAttemptAtLessThanEqualOrderByCreatedDate(Collection<PushOutboxStatus> statuses,
                                                                                    LocalDateTime now,
                                                                                    Pageable pageable);

    /**
     * Outbox 한 건을 발송 중 상태로 원자적으로 선점한다.
     *
     * <p>여러 워커가 같은 후보를 조회했더라도 상태와 재시도 시각 조건을 만족하는 한 워커만 update에 성공한다.
     * 성공 시 상태를 {@code PROCESSING}으로 바꾸고, 실제 Expo 전송 시도 횟수를 1 증가시킨다.</p>
     *
     * @return 선점에 성공하면 1, 다른 워커가 먼저 선점했거나 발송 대상이 아니면 0
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update PushOutbox p
               set p.status = :processingStatus, p.processingStartedAt = :now, p.attemptCount = p.attemptCount + 1
             where p.id = :id
               and p.status in :claimableStatuses
               and p.nextAttemptAt <= :now
            """)
    int claim(@Param("id") Long id,
              @Param("now") LocalDateTime now,
              @Param("processingStatus") PushOutboxStatus processingStatus,
              @Param("claimableStatuses") Collection<PushOutboxStatus> claimableStatuses);

    /**
     * 워커 장애 등으로 일정 시간 이상 {@code PROCESSING}에 머문 Outbox를 재시도 대상으로 복구한다.
     *
     * <p>복구 후 {@code nextAttemptAt}을 현재 시각으로 설정하므로 다음 워커 실행에서 다시 선점·전송할 수 있다.</p>
     *
     * @return 복구된 Outbox 건수
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update PushOutbox p
               set p.status = :retryStatus, p.nextAttemptAt = :now, p.processingStartedAt = null
             where p.status = :processingStatus and p.processingStartedAt < :staleBefore
            """)
    int recoverStaleProcessing(@Param("now") LocalDateTime now,
                               @Param("staleBefore") LocalDateTime staleBefore,
                               @Param("retryStatus") PushOutboxStatus retryStatus,
                               @Param("processingStatus") PushOutboxStatus processingStatus);
}
