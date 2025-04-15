package com.addiction.users.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sex {
	FEMAIL("여성"),
	MAIL("남성");

	private final String text;
}
