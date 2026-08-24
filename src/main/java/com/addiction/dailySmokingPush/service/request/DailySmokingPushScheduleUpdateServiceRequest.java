package com.addiction.dailySmokingPush.service.request;

import com.addiction.common.enums.DailySmokingFeedbackTime;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class DailySmokingPushScheduleUpdateServiceRequest {

    private final DailySmokingFeedbackTime slot;
    private final LocalTime sendTime;
    private final boolean enabled;

    @Builder
    private DailySmokingPushScheduleUpdateServiceRequest(DailySmokingFeedbackTime slot, LocalTime sendTime,
                                                         boolean enabled) {
        this.slot = slot;
        this.sendTime = sendTime;
        this.enabled = enabled;
    }
}
