package com.addiction.user.userCigaretteHistory.service.response;

import com.addiction.user.userCigaretteHistory.enums.CalendarSmokingStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCigaretteHistoryCalenderResponse {

	private final String date;
	private final int count;
    private final CalendarSmokingStatus status;

	@Builder
	public UserCigaretteHistoryCalenderResponse(String date, int count, CalendarSmokingStatus status) {
		this.date = date;
		this.count = count;
        this.status = status;
	}

	public static UserCigaretteHistoryCalenderResponse createResponse(
            String date,
            int count,
            CalendarSmokingStatus status
    ) {
		return new UserCigaretteHistoryCalenderResponse(date, count, status);
	}

}
