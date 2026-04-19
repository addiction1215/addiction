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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ExpoNotiService {

    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    private final RestTemplate restTemplate;
    private final AlertHistoryService alertHistoryService;
    private final ObjectMapper objectMapper;

    public void sendPushNotification(SendFirebaseServiceRequest request) {
        if (request.getPush() == null || request.getPush().getPushToken() == null) {
            alertHistoryService.createAlertHistory(AlertHistoryServiceRequest.of(request));
            return;
        }

        Map<String, Object> body = toPushBody(request);
        callExpoApi(List.of(body));
        alertHistoryService.createAlertHistory(AlertHistoryServiceRequest.of(request));
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

    private void callExpoApi(List<Map<String, Object>> bodies) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");
            headers.set("Accept-Encoding", "gzip, deflate");

            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(bodies, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(EXPO_PUSH_URL, entity, String.class);
            log.info("Expo 푸시 전송 성공 - 건수: {}, status: {}", bodies.size(), response.getStatusCode());
        } catch (Exception e) {
            log.error("Expo 푸시 전송 실패 - message: {}", e.getMessage());
        }
    }
}
