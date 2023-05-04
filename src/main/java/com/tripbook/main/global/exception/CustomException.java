package com.tripbook.main.global.exception;

import com.tripbook.main.global.enums.ErrorCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private ErrorCode errorCode;

	public CustomException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public static class EmailDuplicateException extends CustomException {
		public EmailDuplicateException(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}

	public static class InvalidTokenException extends CustomException {
		public InvalidTokenException(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}
}
