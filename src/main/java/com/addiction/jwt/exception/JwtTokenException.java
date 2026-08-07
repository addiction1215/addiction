package com.addiction.jwt.exception;

import lombok.Getter;

/*
 * JwtTokenException은 HttpStatus가 500이 아니라 401로 내려줘야하기에 따로 생성
 * */
@Getter
public class JwtTokenException extends RuntimeException {
	private final String message;
	private final String errorCode;

	public JwtTokenException(String message, Exception e) {
		this("INVALID_TOKEN", message, e);
	}

	public JwtTokenException(String message) {
		this("INVALID_TOKEN", message);
	}

	public JwtTokenException(String errorCode, String message, Exception e) {
		super(message, e);
		this.message = message;
		this.errorCode = errorCode;
	}

	public JwtTokenException(String errorCode, String message) {
		super(message);
		this.message = message;
		this.errorCode = errorCode;
	}
}
