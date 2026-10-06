package com.addiction.user.users.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BenefitResponse {

    @Schema(description = "금연 유지 일수", example = "18")
    private long nonSmokingDays;
    @Schema(description = "누적 절약 금액(원)", example = "14400")
    private long savedMoney;
    @Schema(description = "하루 절약 금액(원)", example = "800")
    private long dailySavedMoney;

    public static BenefitResponse createResponse(long nonSmokingDays, long savedMoney, long dailySavedMoney) {
        return BenefitResponse.builder()
                .nonSmokingDays(nonSmokingDays)
                .savedMoney(savedMoney)
                .dailySavedMoney(dailySavedMoney)
                .build();
    }
}
