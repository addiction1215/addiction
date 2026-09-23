package com.addiction.smokefree.service.response;

import com.addiction.smokefree.entity.SmokeFreeConfirmation;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SmokeFreeConfirmationResponse {

    private final String date;

    @Builder
    public SmokeFreeConfirmationResponse(String date) {
        this.date = date;
    }

    public static SmokeFreeConfirmationResponse from(SmokeFreeConfirmation confirmation) {
        return SmokeFreeConfirmationResponse.builder()
                .date(confirmation.getConfirmedDate().toString().replace("-", ""))
                .build();
    }
}
