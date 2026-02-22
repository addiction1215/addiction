package com.addiction.user.users.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BenefitResponse {

    private long nonSmokingDays;
    private long savedMoney;
    private long dailySavedMoney;

    public static BenefitResponse createResponse(long nonSmokingDays, long savedMoney, long dailySavedMoney) {
        return BenefitResponse.builder()
                .nonSmokingDays(nonSmokingDays)
                .savedMoney(savedMoney)
                .dailySavedMoney(dailySavedMoney)
                .build();
    }
}
