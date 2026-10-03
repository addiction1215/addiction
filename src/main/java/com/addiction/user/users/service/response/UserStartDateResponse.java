package com.addiction.user.users.service.response;

import java.time.LocalDateTime;

import com.addiction.user.users.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserStartDateResponse {

	@Schema(description = "금연 시작 일시", example = "2026-09-01T09:00:00")
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
