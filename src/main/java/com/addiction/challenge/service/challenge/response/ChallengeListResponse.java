package com.addiction.challenge.service.challenge.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengeListResponse {
    private ChallengeResponse progressingChallenge;
    private List<ChallengeResponse> leftChallengeList;
    private List<ChallengeResponse> finishedChallengeList;

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
