package com.addiction.challenge.service.challenge.response;

import com.addiction.challenge.repository.response.ChallengeDto;
import com.addiction.common.enums.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChallengeResponse{
    private final Long challengeId;
    private final String title;
    private final String content;
    private final String badge;
    private final ChallengeStatus status;

    @Builder
    public ChallengeResponse(Long challengeId, String title, String content, String badge, ChallengeStatus status) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.badge = badge;
        this.status = status;
    }

    public static ChallengeResponse createResponse(ChallengeDto dto) {
        return ChallengeResponse.builder()
                .challengeId(dto.getChallengeId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .badge(dto.getBadge())
                .status(dto.getStatus())
                .build();
    }
}
