package com.addiction.batch.dailySmokingPush;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.alertSetting.entity.enums.AlertType;
import com.addiction.alertSetting.service.AlertSettingReadService;
import com.addiction.common.enums.DailySmokingFeedbackGrade;
import com.addiction.common.enums.DailySmokingFeedbackTime;
import com.addiction.expo.event.PushNotificationEvent;
import com.addiction.firebase.request.SendFirebaseDataDto;
import com.addiction.firebase.request.SendFirebaseServiceRequest;
import com.addiction.user.push.entity.Push;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailySmokingPushBatch {

    private final UserReadService userReadService;
    private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;
    private final AlertSettingReadService alertSettingReadService;
    private final DailySmokingFeedbackMessageSelector messageSelector;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock koreaClock;

    @Scheduled(cron = "0 45 8 * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 30 12,18 * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 0 21 * * *", zone = "Asia/Seoul")
    public void sendDailySmokingFeedback() {
        log.info("=== 정기 흡연 패턴 피드백 배치 시작 ===");

        try {
            LocalDateTime now = LocalDateTime.now(koreaClock);
            LocalDate today = now.toLocalDate();
            LocalDate yesterday = today.minusDays(1);
            String yesterdayStr = yesterday.format(DateTimeFormatter.BASIC_ISO_DATE);

            List<User> users = userReadService.findAllWithPushes();
            log.info("총 {}명의 사용자에게 피드백 전송 시도", users.size());

            List<SendFirebaseServiceRequest> allPushRequests = new ArrayList<>();
            DailySmokingFeedbackTime feedbackTime = DailySmokingFeedbackTime.from(now.toLocalTime());

            for (User user : users) {
                try {
                    if (!shouldSendPush(user)) {
                        log.debug("사용자 {}는 알림 설정이 OFF 상태로 skip", user.getId());
                        continue;
                    }

                    List<Push> pushes = getPushToken(user);
                    if (pushes.isEmpty()) {
                        log.debug("사용자 {}는 Push 토큰이 없어 skip", user.getId());
                        continue;
                    }

                    DailySmokingFeedbackContent feedbackContent;
                    if (shouldSendNewUserMessage(user, today)) {
                        feedbackContent = messageSelector.selectForNewUser(feedbackTime);
                        log.debug("사용자 {}에게 신규 사용자 행동 문구를 선택 - 시간대: {}", user.getId(), feedbackTime);
                    } else {
                        CigaretteHistoryDocument yesterdayData = userCigaretteHistoryRepository
                                .findByDateAndUserId(yesterdayStr, user.getId());

                        int smokeCount = yesterdayData != null && yesterdayData.getSmokeCount() != null
                                ? yesterdayData.getSmokeCount() : 0;
                        long avgPatienceTimeRaw = yesterdayData != null && yesterdayData.getAvgPatienceTime() != null
                                ? yesterdayData.getAvgPatienceTime() : 0L;

                        DailySmokingFeedbackGrade feedbackGrade = DailySmokingFeedbackGrade.from(smokeCount, avgPatienceTimeRaw);
                        feedbackContent = messageSelector.select(feedbackGrade, feedbackTime);
                        log.debug("사용자 {} 푸시 요청 등록 - 흡연 {}회, 평균 금연 유지 {}시간, 등급: {}, 시간대: {}",
                                user.getId(), smokeCount, avgPatienceTimeRaw, feedbackGrade, feedbackTime);
                    }
                    String messageBody = feedbackContent.toPushBody();

                    SendFirebaseDataDto dataDto = SendFirebaseDataDto.builder()
                            .alert_destination_type(AlertDestinationType.DAILY_REPORT)
                            .alert_destination_info("데일리 리포트")
                            .build();

                    pushes.stream()
                            .filter(push -> push != null)
                            .map(push -> SendFirebaseServiceRequest.builder()
                                    .push(push)
                                    .body(messageBody)
                                    .sound("default")
                                    .sendFirebaseDataDto(dataDto)
                                    .build())
                            .forEach(allPushRequests::add);

                } catch (Exception e) {
                    log.error("사용자 {}의 피드백 요청 생성 중 오류 발생", user.getId(), e);
                }
            }

            if (!allPushRequests.isEmpty()) {
                eventPublisher.publishEvent(new PushNotificationEvent(allPushRequests));
                log.info("=== 흡연 패턴 피드백 배치 완료 - 이벤트 발행 건수: {} ===", allPushRequests.size());
            }

        } catch (Exception e) {
            log.error("정기 흡연 패턴 피드백 배치 실행 중 오류 발생", e);
        }
    }

    private boolean shouldSendNewUserMessage(User user, LocalDate today) {
        return user.getFirstSmokingRecordedAt() == null
                || user.getFirstSmokingRecordedAt().toLocalDate().isEqual(today);
    }

    private boolean shouldSendPush(User user) {
        AlertSetting alertSetting = alertSettingReadService.findByUserOrCreateDefault(user);
        if (alertSetting == null) return false;
        if (alertSetting.getAll() == AlertType.OFF) return false;
        return alertSetting.getReport() != AlertType.OFF;
    }

    private List<Push> getPushToken(User user) {
        return user.getPushes() != null ? user.getPushes() : List.of();
    }
}
