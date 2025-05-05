package com.addiction.user.users.service.response;

import java.time.LocalDateTime;

import com.addiction.user.users.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserStartDateResponse {

	private final LocalDateTime startDate;

	@Builder
	public UserStartDateResponse(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public static UserStartDateResponse createResponse(User user) {
		return UserStartDateResponse.builder()
			.startDate(user.getStartDate())
			.build();
	}
}
