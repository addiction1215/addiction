package com.addiction.dailySmokingPush.service.response;

import com.addiction.common.enums.DailySmokingFeedbackTime;
import com.addiction.dailySmokingPush.entity.DailySmokingPushSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class DailySmokingPushScheduleResponse {

    private final DailySmokingFeedbackTime slot;
    private final LocalTime sendTime;
    private final boolean enabled;

    @Builder
    private DailySmokingPushScheduleResponse(DailySmokingFeedbackTime slot, LocalTime sendTime, boolean enabled) {
        this.slot = slot;
        this.sendTime = sendTime;
        this.enabled = enabled;
    }

    public static DailySmokingPushScheduleResponse createResponse(DailySmokingPushSchedule schedule) {
        return DailySmokingPushScheduleResponse.builder()
                .slot(schedule.getSlot())
                .sendTime(schedule.getSendTime())
                .enabled(schedule.isEnabled())
                .build();
    }
}
