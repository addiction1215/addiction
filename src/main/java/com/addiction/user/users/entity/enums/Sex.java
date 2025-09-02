package com.addiction.user.users.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sex {
	FEMALE("여성"),
	MAIL("남성");

	private final String text;
}
