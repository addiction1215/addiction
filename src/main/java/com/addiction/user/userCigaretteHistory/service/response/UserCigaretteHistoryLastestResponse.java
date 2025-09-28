package com.addiction.user.userCigaretteHistory.service.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCigaretteHistoryLastestResponse {

    private final LocalDateTime lastDate;
    private final String address;

    @Builder
    public UserCigaretteHistoryLastestResponse(LocalDateTime lastDate, String address) {
        this.lastDate = lastDate;
        this.address = address;
    }

    public static UserCigaretteHistoryLastestResponse createResponse(LocalDateTime lastDate, String address) {
        return UserCigaretteHistoryLastestResponse.builder()
            .lastDate(lastDate)
            .address(address)
            .build();
    }
}
