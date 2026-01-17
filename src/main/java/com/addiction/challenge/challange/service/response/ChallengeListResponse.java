package com.addiction.challenge.challange.service.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChallengeListResponse {
    private final ChallengeResponse progressingChallenge;
    private final List<ChallengeResponse> leftChallengeList;
    private final List<ChallengeResponse> finishedChallengeList;

    @Builder
    public ChallengeListResponse(ChallengeResponse progressingChallenge, List<ChallengeResponse> leftChallengeList, List<ChallengeResponse> finishedChallengeList) {
        this.progressingChallenge = progressingChallenge;
        this.leftChallengeList = leftChallengeList;
        this.finishedChallengeList = finishedChallengeList;
    }

    public static ChallengeListResponse createResponse(ChallengeResponse progressingChallenge, List<ChallengeResponse> leftChallengeList, List<ChallengeResponse> finishedChallengeList) {
        return ChallengeListResponse.builder()
                .progressingChallenge(progressingChallenge)
                .leftChallengeList(leftChallengeList)
                .finishedChallengeList(finishedChallengeList)
                .build();
    }
}
