package com.addiction.user.users.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmokingTendencyLevel {
    SEVERE(0),
    MODERATE(1),
    MILD(2);

    private final int rank;
}
