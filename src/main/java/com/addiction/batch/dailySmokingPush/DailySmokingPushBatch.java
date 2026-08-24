package com.addiction.batch.dailySmokingPush;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.common.enums.DailySmokingFeedbackGrade;
import com.addiction.alertSetting.service.AlertSettingReadService;
import com.addiction.dailySmokingPush.entity.DailySmokingPushSchedule;
import com.addiction.dailySmokingPush.repository.DailySmokingPushScheduleJpaRepository;
import com.addiction.pushOutbox.service.PushOutboxService;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailySmokingPushBatch {

    private final DailySmokingPushScheduleJpaRepository scheduleRepository;
    private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;
    private final AlertSettingReadService alertSettingReadService;
    private final DailySmokingFeedbackMessageSelector messageSelector;
    private final PushOutboxService pushOutboxService;
    private final Clock koreaClock;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void sendDailySmokingFeedback() {
        log.info("=== 정기 흡연 패턴 피드백 Outbox 생성 시작 ===");

        try {
            // DB의 send_time은 분 단위(예: 12:30:00)이므로, 배치 실행 시각의 초/나노초를 제거해 정확히 비교한다.
            LocalDateTime now = LocalDateTime.now(koreaClock).withSecond(0).withNano(0);
            LocalDate today = now.toLocalDate();
            LocalDate yesterday = today.minusDays(1);
            String yesterdayStr = yesterday.format(DateTimeFormatter.BASIC_ISO_DATE);

            List<DailySmokingPushSchedule> schedules = scheduleRepository.findEnabledDueAt(now.toLocalTime());
            log.info("발송 시각이 된 사용자별 피드백 설정 {}건 조회", schedules.size());

            int createdCount = 0;
            for (DailySmokingPushSchedule schedule : schedules) {
                User user = schedule.getUser();
                try {
                    if (!shouldSendPush(user)) {
                        log.debug("사용자 {}는 알림 설정이 OFF 상태로 skip", user.getId());
                        continue;
                    }

                    if (user.getPushes() == null || user.getPushes().isEmpty()) {
                        log.debug("사용자 {}는 Push 토큰이 없어 skip", user.getId());
                        continue;
                    }

                    DailySmokingFeedbackContent feedbackContent;
                    if (shouldSendNewUserMessage(user, today)) {
                        feedbackContent = messageSelector.selectForNewUser(schedule.getSlot());
                        log.debug("사용자 {}에게 신규 사용자 행동 문구를 선택 - 시간대: {}", user.getId(), schedule.getSlot());
                    } else {
                        CigaretteHistoryDocument yesterdayData = userCigaretteHistoryRepository
                                .findByDateAndUserId(yesterdayStr, user.getId());

                        int smokeCount = yesterdayData != null && yesterdayData.getSmokeCount() != null
                                ? yesterdayData.getSmokeCount() : 0;
                        long avgPatienceTimeRaw = yesterdayData != null && yesterdayData.getAvgPatienceTime() != null
                                ? yesterdayData.getAvgPatienceTime() : 0L;

                        DailySmokingFeedbackGrade feedbackGrade = DailySmokingFeedbackGrade.from(smokeCount, avgPatienceTimeRaw);
                        feedbackContent = messageSelector.select(feedbackGrade, schedule.getSlot());
                        log.debug("사용자 {} 푸시 요청 등록 - 흡연 {}회, 평균 금연 유지 {}시간, 등급: {}, 시간대: {}",
                                user.getId(), smokeCount, avgPatienceTimeRaw, feedbackGrade, schedule.getSlot());
                    }
                    String messageBody = feedbackContent.toPushBody();

                    if (pushOutboxService.createPendingIfAbsent(
                            schedule, today, messageBody, AlertDestinationType.DAILY_REPORT, "데일리 리포트", now)) {
                        createdCount++;
                    }

                } catch (Exception e) {
                    log.error("사용자 {}의 피드백 요청 생성 중 오류 발생", user.getId(), e);
                }
            }

            log.info("=== 흡연 패턴 피드백 Outbox 생성 완료 - 생성 건수: {} ===", createdCount);

        } catch (Exception e) {
            log.error("정기 흡연 패턴 피드백 배치 실행 중 오류 발생", e);
        }
    }

    private boolean shouldSendNewUserMessage(User user, LocalDate today) {
        return user.getFirstSmokingRecordedAt() == null
                || user.getFirstSmokingRecordedAt().toLocalDate().isEqual(today);
    }

    private boolean shouldSendPush(User user) {
        return alertSettingReadService.isReportPushEnabled(user);
    }
}
