package com.addiction.challenge.challengehistory.service.response;

import com.addiction.challenge.challengehistory.entity.ChallengeHistory;
import com.addiction.challenge.challengehistory.entity.ChallengeStatus;
import com.addiction.challenge.missionhistory.entity.MissionHistory;
import com.addiction.challenge.missionhistory.entity.MissionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 챌린지 참여 응답
 */
@Getter
public class ChallengeJoinResponse {
    private final ChallengeHistoryInfo challengeHistory;
    private final List<MissionHistoryInfo> missionHistories;

    @Builder
    public ChallengeJoinResponse(ChallengeHistoryInfo challengeHistory, List<MissionHistoryInfo> missionHistories) {
        this.challengeHistory = challengeHistory;
        this.missionHistories = missionHistories;
    }

    @Getter
    public static class ChallengeHistoryInfo {
        private final Long id;
        private final Long challengeId;
        private final String challengeTitle;
        private final String challengeContent;
        private final ChallengeStatus status;
        private final LocalDateTime createdDate;

        @Builder
        public ChallengeHistoryInfo(Long id, Long challengeId, String challengeTitle, String challengeContent, ChallengeStatus status, LocalDateTime createdDate) {
            this.id = id;
            this.challengeId = challengeId;
            this.challengeTitle = challengeTitle;
            this.challengeContent = challengeContent;
            this.status = status;
            this.createdDate = createdDate;
        }
    }

    @Getter
    public static class MissionHistoryInfo {
        private final Long id;
        private final Long missionId;
        private final String missionTitle;
        private final MissionStatus status;
        private final LocalDateTime createdDate;

        @Builder
        public MissionHistoryInfo(Long id, Long missionId, String missionTitle, MissionStatus status, LocalDateTime createdDate) {
            this.id = id;
            this.missionId = missionId;
            this.missionTitle = missionTitle;
            this.status = status;
            this.createdDate = createdDate;
        }
    }

    public static ChallengeJoinResponse of(ChallengeHistory challengeHistory, List<MissionHistory> missionHistories) {
        ChallengeHistoryInfo challengeHistoryInfo = ChallengeHistoryInfo.builder()
                .id(challengeHistory.getId())
                .challengeId(challengeHistory.getChallenge().getId())
                .challengeTitle(challengeHistory.getChallenge().getTitle())
                .challengeContent(challengeHistory.getChallenge().getContent())
                .status(challengeHistory.getStatus())
                .createdDate(challengeHistory.getCreatedDate())
                .build();

        List<MissionHistoryInfo> missionHistoryInfos = missionHistories.stream()
                .map(mh -> MissionHistoryInfo.builder()
                        .id(mh.getId())
                        .missionId(mh.getMission().getId())
                        .missionTitle(mh.getMission().getTitle())
                        .status(mh.getStatus())
                        .createdDate(mh.getCreatedDate())
                        .build())
                .collect(Collectors.toList());

        return ChallengeJoinResponse.builder()
                .challengeHistory(challengeHistoryInfo)
                .missionHistories(missionHistoryInfos)
                .build();
    }
}
