package com.addiction.expo;

import com.addiction.alertHistory.service.alertHistory.AlertHistoryService;
import com.addiction.alertHistory.service.alertHistory.request.AlertHistoryServiceRequest;
import com.addiction.firebase.enums.PushMessage;
import com.addiction.firebase.request.SendFirebaseServiceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ExpoNotiService {

    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    private final RestTemplate restTemplate;
    private final AlertHistoryService alertHistoryService;
    private final ObjectMapper objectMapper;

    /**
     * 일반 푸시용 전송 메서드입니다.
     *
     * <p>친구 요청처럼 재시도 대상을 DB에 별도로 보관하지 않는 푸시는 전송 실패를 로그로만 남기고,
     * 호출 흐름이 실패하지 않도록 합니다.</p>
     */
    public void sendBatchPushNotificationSafely(List<SendFirebaseServiceRequest> requests) {
        try {
            sendBatchPushNotification(requests);
        } catch (Exception e) {
            log.error("Expo 푸시 전송 실패 - message: {}", e.getMessage(), e);
        }
    }

    /**
     * Outbox 워커 전용 전송 메서드입니다.
     *
     * <p>전송 실패를 호출자에게 전달해야 Outbox를 {@code RETRY}/{@code FAILED}로 변경할 수 있으므로,
     * 예외를 잡지 않습니다.</p>
     */
    public void sendBatchPushNotificationForOutbox(List<SendFirebaseServiceRequest> requests) {
        sendBatchPushNotification(requests);
    }

    /**
     * Expo 전송이 성공한 경우에만 앱 내 알림 이력을 생성합니다.
     */
    private void sendBatchPushNotification(List<SendFirebaseServiceRequest> requests) {
        if (requests.isEmpty()) return;

        List<Map<String, Object>> bodies = requests.stream()
                .filter(r -> r.getPush() != null && r.getPush().getPushToken() != null)
                .map(this::toPushBody)
                .toList();

        if (bodies.isEmpty()) return;
        sendToExpo(bodies);
        requests.stream()
                .filter(r -> r.getPush() != null)
                .collect(Collectors.toMap(
                        r -> r.getPush().getUser().getId(),
                        r -> r,
                        (existing, replacement) -> existing
                ))
                .values()
                .forEach(r -> alertHistoryService.createAlertHistory(AlertHistoryServiceRequest.of(r)));
    }

    private Map<String, Object> toPushBody(SendFirebaseServiceRequest request) {
        Map<String, Object> data = objectMapper.convertValue(request.getSendFirebaseDataDto(), Map.class);

        return Map.of(
                "to", request.getPush().getPushToken(),
                "title", PushMessage.TITLE.getText(),
                "body", request.getBody(),
                "sound", request.getSound(),
                "data", data
        );
    }

    /**
     * Expo HTTP 호출 자체만 담당합니다. 실패 예외는 정책을 결정하는 상위 메서드로 전달합니다.
     */
    private void sendToExpo(List<Map<String, Object>> bodies) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");
        headers.set("Accept-Encoding", "gzip, deflate");

        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(bodies, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(EXPO_PUSH_URL, entity, String.class);
        log.info("Expo 푸시 전송 성공 - 건수: {}, status: {}", bodies.size(), response.getStatusCode());
    }
}
