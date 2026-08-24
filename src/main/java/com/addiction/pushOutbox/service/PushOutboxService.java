package com.addiction.pushOutbox.service;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.dailySmokingPush.entity.DailySmokingPushSchedule;
import com.addiction.pushOutbox.entity.PushOutbox;
import com.addiction.pushOutbox.entity.PushOutboxStatus;
import com.addiction.pushOutbox.repository.PushOutboxJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 푸시 Outbox의 상태 전이를 관리한다.
 *
 * <p>일일 피드백 배치는 이 서비스를 통해 발송 대기 건만 생성하고, 발송 워커는 후보 조회·선점·성공·실패 처리를
 * 이 서비스에 위임한다. 외부 API 호출은 담당하지 않아 DB 상태 관리와 전송 책임을 분리한다.</p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PushOutboxService {

    private static final int MAX_ATTEMPTS = 5;
    private final PushOutboxJpaRepository pushOutboxRepository;

    /**
     * 동일한 사용자 설정과 발송 날짜의 대기 건이 없을 때만 {@code PENDING} Outbox를 생성한다.
     *
     * @return 새 대기 건을 생성하면 {@code true}, 이미 생성되어 있으면 {@code false}
     */
    public boolean createPendingIfAbsent(DailySmokingPushSchedule schedule, LocalDate deliveryDate,
                                         String body, AlertDestinationType destinationType,
                                         String destinationInfo, LocalDateTime now) {
        return pushOutboxRepository.insertPendingIfAbsent(
                schedule.getUser().getId(), schedule.getId(), deliveryDate, body,
                destinationType.name(), destinationInfo, now
        ) == 1;
    }

    /**
     * 현재 시각에 발송 또는 재시도가 가능한 Outbox 후보를 제한된 개수만큼 조회한다.
     */
    @Transactional(readOnly = true)
    public List<PushOutbox> findDispatchCandidates(LocalDateTime now, int limit) {
        return pushOutboxRepository.findByStatusInAndNextAttemptAtLessThanEqualOrderByCreatedDate(
                List.of(PushOutboxStatus.PENDING, PushOutboxStatus.RETRY), now, PageRequest.of(0, limit));
    }

    /**
     * 후보 Outbox를 {@code PROCESSING} 상태로 선점한다.
     *
     * <p>다중 서버 환경에서는 같은 후보를 여러 워커가 볼 수 있으므로, 실제 전송 전에 반드시 이 메서드의
     * 반환값이 {@code true}인지 확인해야 한다.</p>
     */
    public boolean claim(Long outboxId, LocalDateTime now) {
        return pushOutboxRepository.claim(outboxId, now, PushOutboxStatus.PROCESSING,
                List.of(PushOutboxStatus.PENDING, PushOutboxStatus.RETRY)) == 1;
    }

    /**
     * Expo 전송 성공 후 Outbox를 {@code SENT} 상태로 변경한다.
     */
    public void markSent(Long outboxId, LocalDateTime now) {
        PushOutbox outbox = pushOutboxRepository.findById(outboxId).orElseThrow();
        outbox.markSent(now);
    }

    /**
     * Expo 전송 실패를 기록하고 재시도 시각을 정한다.
     *
     * <p>시도 횟수가 최대 횟수에 도달하면 {@code FAILED}로 종료하고, 그렇지 않으면
     * 2^시도횟수 분 뒤에 다시 시도하도록 {@code RETRY} 상태로 변경한다.</p>
     */
    public void retryOrFail(Long outboxId, LocalDateTime now, String errorMessage) {
        PushOutbox outbox = pushOutboxRepository.findById(outboxId).orElseThrow();
        if (outbox.getAttemptCount() >= MAX_ATTEMPTS) {
            outbox.markFailed(errorMessage);
            return;
        }
        outbox.retryAt(now.plusMinutes((long) Math.pow(2, outbox.getAttemptCount())), errorMessage);
    }

    /**
     * 워커 장애로 10분 이상 {@code PROCESSING} 상태에 머문 건을 재시도 대상으로 되돌린다.
     */
    public int recoverStaleProcessing(LocalDateTime now) {
        return pushOutboxRepository.recoverStaleProcessing(now, now.minusMinutes(10),
                PushOutboxStatus.RETRY, PushOutboxStatus.PROCESSING);
    }
}
