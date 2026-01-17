package com.addiction.user.users.service.request;

import java.util.List;

import com.addiction.user.users.entity.enums.Sex;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateSurveyServiceRequest {
	private final List<Long> answerId;
	private final String purpose;
	private final Integer cigarettePrice;
    private final Integer cigaretteCount;

	@Builder
	public UserUpdateSurveyServiceRequest(List<Long> answerId, String purpose, Integer cigarettePrice, Integer cigaretteCount) {
		this.answerId = answerId;
		this.purpose = purpose;
		this.cigarettePrice = cigarettePrice;
        this.cigaretteCount = cigaretteCount;
	}
}
