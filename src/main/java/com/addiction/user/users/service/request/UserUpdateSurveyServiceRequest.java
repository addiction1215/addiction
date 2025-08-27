package com.addiction.user.users.service.request;

import java.util.List;

import com.addiction.user.users.entity.enums.Sex;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateSurveyServiceRequest {
	private final List<Long> answerId;
	private final String purpose;
	private final int cigarettePrice;

	@Builder
	public UserUpdateSurveyServiceRequest(List<Long> answerId, String purpose, int cigarettePrice) {
		this.answerId = answerId;
		this.purpose = purpose;
		this.cigarettePrice = cigarettePrice;
	}
}
