package com.addiction.user.users.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sex {
	@Schema(description = "여성")
	FEMALE("여성"),
	@Schema(description = "남성")
    MALE("남성");

	private final String text;
}
