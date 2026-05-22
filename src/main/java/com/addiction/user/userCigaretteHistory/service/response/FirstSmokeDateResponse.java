package com.addiction.user.userCigaretteHistory.service.response;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FirstSmokeDateResponse {

    private final LocalDateTime firstDate;

    public static FirstSmokeDateResponse createResponse(CigaretteHistoryDocument document) {
        return FirstSmokeDateResponse.builder()
                .firstDate(document.getSmokeDate())
                .build();
    }
}
