package com.addiction.dailySmokingPush.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DailySmokingPushScheduleUpdateBatchRequest {

    @NotEmpty
    private List<@Valid DailySmokingPushScheduleUpdateRequest> schedules;

    @Builder
    public DailySmokingPushScheduleUpdateBatchRequest(List<DailySmokingPushScheduleUpdateRequest> schedules) {
        this.schedules = schedules;
    }
}
