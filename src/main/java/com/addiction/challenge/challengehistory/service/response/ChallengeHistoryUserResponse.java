package com.addiction.challenge.challengehistory.service.response;

import com.addiction.challenge.challengehistory.repository.response.ChallengeHistoryUserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeHistoryUserResponse {

    private final Long challengeId;
    private final String badge;
    private final String title;

    @Builder
    public ChallengeHistoryUserResponse(Long challengeId, String badge, String title) {
        this.challengeId = challengeId;
        this.badge = badge;
        this.title = title;
    }

    public static ChallengeHistoryUserResponse createResponse(ChallengeHistoryUserDto dto) {
        return ChallengeHistoryUserResponse.builder()
                .challengeId(dto.getChallengeId())
                .badge(dto.getBadge())
                .title(dto.getTitle())
                .build();
    }
}
