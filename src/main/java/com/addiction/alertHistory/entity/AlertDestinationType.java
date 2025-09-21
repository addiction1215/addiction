package com.addiction.alertHistory.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertDestinationType {

	FRIEND("친구 추가 알림"),
	FRIEND_CODE("친구 추가요청 수신알림");

	private final String text;
}
