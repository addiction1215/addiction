package com.addiction.firebase;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.alertHistory.service.alertHistory.AlertHistoryService;
import com.addiction.alertHistory.service.alertHistory.request.AlertHistoryServiceRequest;
import com.addiction.firebase.enums.PushMessage;
import com.addiction.firebase.request.SendFirebaseDataDto;
import com.addiction.firebase.request.SendFirebaseServiceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FirebaseService {

    // private final FirebaseMessaging firebaseMessaging;
    private final AlertHistoryService alertHistoryService;
    private final ObjectMapper objectMapper;

    public void sendPushNotification(SendFirebaseServiceRequest sendPushServiceRequest, String pushToken) {
        try {
            // PushToken이 있으면 푸시전송
            if (pushToken != null) {
                Message message = Message.builder()
                        .setApnsConfig(ApnsConfig.builder()
                                .setAps(Aps.builder()
                                        .putAllCustomData(ObjectToMap(sendPushServiceRequest.getSendFirebaseDataDto()))
                                        .setSound(sendPushServiceRequest.getSound())
                                        .setAlert(ApsAlert.builder()
                                                .setTitle(PushMessage.TITLE.getText())
                                                .setBody(sendPushServiceRequest.getBody())
                                                .build())
                                        .build())
                                .build())
                        .setToken(pushToken)
                        .build();

                // firebaseMessaging.send(message);
            }
        }
        // catch (FirebaseMessagingException e){
        //     log.error("Token: "+ pushToken + "Error: " + e.getMessage());
        // }
        finally {
            //히스토리는 무조건 쌓는걸로
            alertHistoryService.createAlertHistory(AlertHistoryServiceRequest.of(sendPushServiceRequest));
        }

    }

    private Map<String, Object> ObjectToMap(SendFirebaseDataDto sendFirebaseDataDto){
        return objectMapper.convertValue(sendFirebaseDataDto, Map.class);
    }
}
