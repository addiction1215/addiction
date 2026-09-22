package com.addiction.user.users.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CumulativeChangeResponse {

    private final long savedMoney;
    private final long reducedCigaretteCount;
    private final Long longestAbstinenceSeconds;
}
