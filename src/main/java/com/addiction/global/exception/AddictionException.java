package com.addiction.global.exception;

import lombok.Getter;

@Getter
public class AddictionException extends RuntimeException {

	private final String message;

	public AddictionException(String message, Exception e) {
		super(message, e);
		this.message = message;
	}

	public AddictionException(String message) {
		this.message = message;
	}
}
