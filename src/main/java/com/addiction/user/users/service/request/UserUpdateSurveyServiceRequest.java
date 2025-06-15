package com.addiction.user.users.service.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateSurveyServiceRequest {
	private final String nickName;
	private final List<Integer> answerId;
	private final String purpose;
	private final int cigarettePrice;

	@Builder
	public UserUpdateSurveyServiceRequest(String nickName, List<Integer> answerId, String purpose, int cigarettePrice) {
		this.nickName = nickName;
		this.answerId = answerId;
		this.purpose = purpose;
		this.cigarettePrice = cigarettePrice;
	}
}
