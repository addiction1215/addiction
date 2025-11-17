package com.addiction.batch.dailySmokingPush;

import com.addiction.alertHistory.entity.AlertDestinationType;
import com.addiction.alertSetting.entity.AlertSetting;
import com.addiction.alertSetting.entity.enums.AlertType;
import com.addiction.alertSetting.repository.AlertSettingJpaRepository;
import com.addiction.common.enums.DailySmokingPushMessage;
import com.addiction.firebase.FirebaseService;
import com.addiction.firebase.enums.PushMessage;
import com.addiction.firebase.request.SendFirebaseDataDto;
import com.addiction.firebase.request.SendFirebaseServiceRequest;
import com.addiction.user.push.entity.Push;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 매일 아침 7시에 실행되는 흡연 패턴 피드백 배치
 * 전날 흡연 데이터를 기반으로 사용자에게 맞춤형 피드백 메시지를 전송
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DailySmokingPushBatch {

    private final UserReadService userReadService;
    private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;
    private final AlertSettingJpaRepository alertSettingJpaRepository;
    private final FirebaseService firebaseService;

    /**
     * 매일 아침 7시에 실행
     * cron: 초 분 시 일 월 요일
     * "0 0 7 * * *" = 매일 7시 0분 0초
     */
    @Scheduled(cron = "0 0 7 * * *")
    public void sendDailySmokingFeedback() {
        log.info("=== 매일 아침 흡연 패턴 피드백 배치 시작 ===");

        try {
            // 전날 날짜 계산
            LocalDate yesterday = LocalDate.now().minusDays(1);
            String yesterdayStr = yesterday.format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd

            // 모든 활성 사용자 조회
            List<User> users = userReadService.findAll();
            log.info("총 {}명의 사용자에게 피드백 전송 시도", users.size());

            for (User user : users) {
                try {
                    // 알림 설정 확인
                    if (!shouldSendPush(user)) {
                        log.debug("사용자 {}는 알림 설정이 OFF 상태로 skip", user.getId());
                        continue;
                    }

                    // Push 토큰 확인
                    for (Push push : Objects.requireNonNull(getPushToken(user))) {
                        if (push == null) {
                            log.debug("사용자 {}는 Push 토큰이 없어 skip", user.getId());
                            continue;
                        }

                        // 전날 흡연 데이터 조회
                        CigaretteHistoryDocument yesterdayData = userCigaretteHistoryRepository
                                .findByDateAndUserId(yesterdayStr, user.getId());

                        if (yesterdayData == null) {
                            log.debug("사용자 {}의 전날 흡연 데이터가 없어 skip", user.getId());
                            continue;
                        }

                        // 흡연 횟수 및 평균 금연 유지 시간 추출
                        int smokeCount = yesterdayData.getSmokeCount() != null ? yesterdayData.getSmokeCount() : 0;
                        long avgPatienceTimeRaw = yesterdayData.getAvgPatienceTime() != null ? yesterdayData.getAvgPatienceTime() : 0L;

                        // avgPatienceTime을 시간 단위로 변환
                        // MongoDB에 저장된 값이 시간 단위가 아닐 경우 아래 주석 해제하여 변환
//                        long avgPatienceTime = avgPatienceTimeRaw;

                        // 밀리초 단위인 경우:
                        // long avgPatienceTime = avgPatienceTimeRaw / (1000 * 60 * 60);

                        // 초 단위인 경우:
                        // long avgPatienceTime = avgPatienceTimeRaw / 3600;

                        // 분 단위인 경우:
                        // long avgPatienceTime = avgPatienceTimeRaw / 60;

                        DailySmokingPushMessage selectedMessage = DailySmokingPushMessage.selectMessage(smokeCount, avgPatienceTimeRaw);

                        // Push 알림 전송
                        sendPushNotification(selectedMessage.getMessage(), push);

                        log.debug("사용자 {} 피드백 전송 완료 - 흡연 {}회, 평균 금연 유지 {}시간, 메시지: {}",
                                user.getId(), smokeCount, avgPatienceTimeRaw, selectedMessage.name());
                    }
                } catch (Exception e) {
                    log.error("사용자 {}의 피드백 전송 중 오류 발생", user.getId(), e);
                }
            }
        } catch (Exception e) {
            log.error("매일 아침 흡연 패턴 피드백 배치 실행 중 오류 발생", e);
        }
    }

    /**
     * 사용자에게 Push 알림을 전송해야 하는지 확인
     * - 전체 알림 설정 확인
     * - 리포트 알림 설정 확인
     */
    private boolean shouldSendPush(User user) {
        AlertSetting alertSetting = alertSettingJpaRepository.findByUser(user).orElse(null);

        if (alertSetting == null) {
            return false;
        }

        // 전체 알림이 OFF면 전송하지 않음
        if (alertSetting.getAll() == AlertType.OFF) {
            return false;
        }

        // 리포트 알림이 OFF면 전송하지 않음
        return alertSetting.getReport() != AlertType.OFF;
    }

    /**
     * 사용자의 Push 토큰 조회
     */
    private List<Push> getPushToken(User user) {
        List<Push> pushes = user.getPushes();

        if (pushes == null || pushes.isEmpty()) {
            return null;
        }

        return pushes;
    }

    /**
     * Firebase Push 알림 전송
     */
    private void sendPushNotification(String message, Push push) {
//        SendFirebaseDataDto dataDto = SendFirebaseDataDto.builder()
//                .alert_destination_type(AlertDestinationType.DAILY_REPORT)
//                .alert_destination_info()
//                .build();

        SendFirebaseServiceRequest serviceRequest = SendFirebaseServiceRequest.builder()
                .push(push)
                .body(message)
                .sound("default")
                .sendFirebaseDataDto(null)
                .build();

        firebaseService.sendPushNotification(serviceRequest);
    }
}
