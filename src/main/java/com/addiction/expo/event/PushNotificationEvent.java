package com.addiction.expo.event;

import com.addiction.firebase.request.SendFirebaseServiceRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class PushNotificationEvent {

    private final List<SendFirebaseServiceRequest> requests;

    public PushNotificationEvent(List<SendFirebaseServiceRequest> requests) {
        this.requests = requests;
    }
}
