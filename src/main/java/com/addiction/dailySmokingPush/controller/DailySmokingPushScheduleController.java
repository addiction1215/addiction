package com.addiction.dailySmokingPush.controller;

import com.addiction.dailySmokingPush.controller.request.DailySmokingPushScheduleUpdateBatchRequest;
import com.addiction.dailySmokingPush.controller.request.DailySmokingPushScheduleUpdateRequest;
import com.addiction.dailySmokingPush.service.DailySmokingPushScheduleService;
import com.addiction.dailySmokingPush.service.response.DailySmokingPushScheduleResponse;
import com.addiction.global.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/daily-smoking-push-schedules")
public class DailySmokingPushScheduleController {

    private final DailySmokingPushScheduleService scheduleService;

    @GetMapping
    public ApiResponse<List<DailySmokingPushScheduleResponse>> getSchedules() {
        return ApiResponse.ok(scheduleService.getMySchedules());
    }

    @PutMapping
    public ApiResponse<List<DailySmokingPushScheduleResponse>> updateSchedules(
            @RequestBody @Valid DailySmokingPushScheduleUpdateBatchRequest request) {
        return ApiResponse.ok(scheduleService.updateMySchedules(
                request.getSchedules().stream().map(DailySmokingPushScheduleUpdateRequest::toServiceRequest).toList()
        ));
    }
}
