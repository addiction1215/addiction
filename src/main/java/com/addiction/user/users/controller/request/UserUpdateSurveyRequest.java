package com.addiction.user.users.controller.request;

import java.util.List;

import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.service.request.UserUpdateSurveyServiceRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateSurveyRequest {
	@NotNull(message = "답변 ID 목록은 필수입니다.")
	@Size(min = 1, message = "답변 ID 목록은 최소 1개 이상이어야 합니다.")
	private List<Long> answerId;
	@NotNull(message = "금연 목표는 필수입니다.")
	private String purpose;
	@Min(value = 1, message = "담배 가격은 0원 초과이어야 합니다.")
	private int cigarettePrice;

	@Builder
	public UserUpdateSurveyRequest(List<Long> answerId, String purpose, int cigarettePrice) {
		this.answerId = answerId;
		this.purpose = purpose;
		this.cigarettePrice = cigarettePrice;
	}

	public UserUpdateSurveyServiceRequest toServiceRequest() {
		return UserUpdateSurveyServiceRequest.builder()
			.answerId(answerId)
			.purpose(purpose)
			.cigarettePrice(cigarettePrice)
			.build();
	}
}
