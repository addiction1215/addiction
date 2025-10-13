package com.addiction.challenge.service.challenge.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengeResponse {
    private ProgressingChallenge progressingChallenge;
    private List<ChallengeResponseList> leftChallengeList;
    private List<ChallengeResponseList> finishedChallengeList;

    @Builder
    public ChallengeResponse(ProgressingChallenge progressingChallenge, List<ChallengeResponseList> leftChallengeList, List<ChallengeResponseList> finishedChallengeList) {
        this.progressingChallenge = progressingChallenge;
        this.leftChallengeList = leftChallengeList;
        this.finishedChallengeList = finishedChallengeList;
    }
}
