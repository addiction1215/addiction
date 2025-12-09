package com.addiction.challengehistory.Controller;

import com.addiction.challengehistory.service.ChallengeHistoryReadService;
import com.addiction.challengehistory.service.ChallengeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge-history")
public class ChallengeHistoryController {
    private final ChallengeHistoryService challengeHistoryService;
    private final ChallengeHistoryReadService challengeHistoryReadService;
}
