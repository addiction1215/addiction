package com.addiction.user.users.entity.enums;

import java.util.Arrays;
import java.util.Optional;

import com.addiction.global.exception.AddictionException;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SnsType {
	@Schema(description = "일반 이메일 가입")
	NORMAL("일반"),
	@Schema(description = "카카오 로그인")
	KAKAO("카카오"),
	@Schema(description = "구글 로그인")
	GOOGLE("구글"),
	@Schema(description = "네이버 로그인")
	NAVER("네이버");

	private final String text;

	public void checkSnsType() {
		Optional<SnsType> snsType = Arrays.stream(values())
			.filter(type -> type.getText().equals(text))
			.findFirst();
		snsType.ifPresent(type -> {
			throw new AddictionException(type.name());
		});
	}
}
