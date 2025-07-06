package com.addiction.user.users.service.request;

import java.util.List;

import com.addiction.user.users.entity.enums.Sex;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateSurveyServiceRequest {
	private final List<Integer> answerId;
	private final String purpose;
	private final int cigarettePrice;
	private final Sex sex;
	private final String birthDay;

	@Builder
	public UserUpdateSurveyServiceRequest(List<Integer> answerId, String purpose, int cigarettePrice, Sex sex,
		String birthDay) {
		this.answerId = answerId;
		this.purpose = purpose;
		this.cigarettePrice = cigarettePrice;
		this.sex = sex;
		this.birthDay = birthDay;
	}
}
