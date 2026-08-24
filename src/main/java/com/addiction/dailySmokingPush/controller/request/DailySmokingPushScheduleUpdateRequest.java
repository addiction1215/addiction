package com.addiction.dailySmokingPush.controller.request;

import com.addiction.common.enums.DailySmokingFeedbackTime;
import com.addiction.dailySmokingPush.service.request.DailySmokingPushScheduleUpdateServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class DailySmokingPushScheduleUpdateRequest {

    @NotNull
    private DailySmokingFeedbackTime slot;

    @NotNull
    private LocalTime sendTime;

    @NotNull
    private Boolean enabled;

    @Builder
    public DailySmokingPushScheduleUpdateRequest(DailySmokingFeedbackTime slot, LocalTime sendTime, Boolean enabled) {
        this.slot = slot;
        this.sendTime = sendTime;
        this.enabled = enabled;
    }

    public DailySmokingPushScheduleUpdateServiceRequest toServiceRequest() {
        return DailySmokingPushScheduleUpdateServiceRequest.builder()
                .slot(slot)
                .sendTime(sendTime)
                .enabled(enabled)
                .build();
    }
}
