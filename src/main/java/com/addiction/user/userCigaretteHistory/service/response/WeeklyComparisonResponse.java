package com.addiction.user.userCigaretteHistory.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyComparisonResponse {

    private Integer lastWeekCount;
    private Integer thisWeekCount;
    private Double countDifference;
    private Double countChangeRate;

    private Double lastWeekAvgTime;
    private Double thisWeekAvgTime;
    private Double timeDifference;
    private Double timeChangeRate;

    public static WeeklyComparisonResponse createResponse(
            int lastWeekCount, int thisWeekCount, double countDifference, double countChangeRate,
            double lastWeekAvgTime, double thisWeekAvgTime, double timeDifference, double timeChangeRate) {
        return WeeklyComparisonResponse.builder()
                .lastWeekCount(lastWeekCount)
                .thisWeekCount(thisWeekCount)
                .countDifference(countDifference)
                .countChangeRate(countChangeRate)
                .lastWeekAvgTime(lastWeekAvgTime)
                .thisWeekAvgTime(thisWeekAvgTime)
                .timeDifference(timeDifference)
                .timeChangeRate(timeChangeRate)
                .build();
    }
}
