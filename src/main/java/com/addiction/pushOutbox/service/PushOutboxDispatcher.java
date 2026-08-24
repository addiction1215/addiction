package com.addiction.pushOutbox.service;

import com.addiction.expo.ExpoNotiService;
import com.addiction.firebase.request.SendFirebaseDataDto;
import com.addiction.firebase.request.SendFirebaseServiceRequest;
import com.addiction.pushOutbox.entity.PushOutbox;
import com.addiction.user.push.entity.Push;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushOutboxDispatcher {

    private static final int DISPATCH_BATCH_SIZE = 100;

    private final PushOutboxService pushOutboxService;
    private final ExpoNotiService expoNotiService;
    private final Clock koreaClock;

    @Scheduled(fixedDelay = 5_000)
    public void dispatch() {
        LocalDateTime now = LocalDateTime.now(koreaClock);
        pushOutboxService.recoverStaleProcessing(now);

        List<PushOutbox> candidates = pushOutboxService.findDispatchCandidates(now, DISPATCH_BATCH_SIZE);
        for (PushOutbox outbox : candidates) {
            if (!pushOutboxService.claim(outbox.getId(), now)) {
                continue;
            }

            try {
                List<SendFirebaseServiceRequest> requests = outbox.getUser().getPushes().stream()
                        .filter(push -> push != null && push.getPushToken() != null)
                        .map(push -> toRequest(push, outbox))
                        .toList();

                // Outbox는 실패 시 재시도해야 하므로 Expo 전송 예외를 그대로 받는다.
                expoNotiService.sendBatchPushNotificationForOutbox(requests);
                pushOutboxService.markSent(outbox.getId(), LocalDateTime.now(koreaClock));
            } catch (Exception e) {
                log.warn("Outbox 푸시 발송 실패 - outboxId: {}", outbox.getId(), e);
                pushOutboxService.retryOrFail(outbox.getId(), LocalDateTime.now(koreaClock), e.getMessage());
            }
        }
    }

    private SendFirebaseServiceRequest toRequest(Push push, PushOutbox outbox) {
        return SendFirebaseServiceRequest.builder()
                .push(push)
                .body(outbox.getBody())
                .sound("default")
                .sendFirebaseDataDto(SendFirebaseDataDto.builder()
                        .alert_destination_type(outbox.getDestinationType())
                        .alert_destination_info(outbox.getDestinationInfo())
                        .build())
                .build();
    }
}
