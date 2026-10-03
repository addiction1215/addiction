package com.addiction.user.users.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CumulativeChangeResponse {

    @Schema(description = "가입 이후 확인된 금연일 기준 누적 절약 금액(원)", example = "14400")
    private final long savedMoney;
    @Schema(description = "가입 이후 확인된 금연일 기준 덜 피운 담배 개비 수", example = "32")
    private final long reducedCigaretteCount;
    @Schema(description = "마지막 흡연 날짜부터 오늘까지의 연속 금연 일수. 흡연 기록이 없으면 금연 시작 날짜 기준입니다.", example = "8")
    private final long nonSmokingDays;
    @Schema(description = "가입 이후 금연 성공으로 확인된 누적 날짜 수", example = "42")
    private final long totalSmokeFreeDays;
    @Schema(description = "역대 최장 연속 금연 시간(초). 흡연 기록이 없으면 null입니다.", example = "111600", nullable = true)
    private final Long longestAbstinenceSeconds;
}
