package com.addiction.smokefree.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SmokeFreeConfirmationServiceRequest {

    private final String date;

    @Builder
    public SmokeFreeConfirmationServiceRequest(String date) {
        this.date = date;
    }
}
