package com.addiction.user.users.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmokingTendencyLevel {
    @Schema(description = "중증 흡연 성향, QuitMate 점수 0~39점")
    SEVERE(0),
    @Schema(description = "중등도 흡연 성향, QuitMate 점수 40~59점")
    MODERATE(1),
    @Schema(description = "경증 흡연 성향, QuitMate 점수 60~100점")
    MILD(2);

    private final int rank;
}
