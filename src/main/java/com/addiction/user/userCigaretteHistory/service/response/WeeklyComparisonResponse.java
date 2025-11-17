package com.addiction.user.userCigaretteHistory.service.response;

import com.addiction.user.userCigaretteHistory.enums.ComparisonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주간 비교 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyComparisonResponse {
    
    /**
     * 비교 타입 (COUNT: 횟수, TIME: 시간)
     */
    private ComparisonType comparisonType;
    
    /**
     * 지난주 총 횟수 (COUNT 타입인 경우)
     */
    private Integer lastWeekCount;
    
    /**
     * 이번주 총 횟수 (COUNT 타입인 경우)
     */
    private Integer thisWeekCount;
    
    /**
     * 지난주 평균 금연 시간 (TIME 타입인 경우, 단위: 시간)
     */
    private Double lastWeekAvgTime;
    
    /**
     * 이번주 평균 금연 시간 (TIME 타입인 경우, 단위: 시간)
     */
    private Double thisWeekAvgTime;
    
    /**
     * 증감 값 (양수: 증가, 음수: 감소)
     * COUNT 타입: 횟수 차이
     * TIME 타입: 시간 차이
     */
    private Double difference;
    
    /**
     * 증감률 (%)
     */
    private Double changeRate;
}
