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

	public static class AccessDeniedException extends CustomException {
		public AccessDeniedException(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}

	public static class CsrfException extends CustomException {
		public CsrfException(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}

	public static class AuthenticationException extends CustomException {
		public AuthenticationException(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}

	public static class SecurityException extends CustomException {
		public SecurityException(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}

	public static class UnsupportedPlatform extends CustomException {
		public UnsupportedPlatform(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}

	public static class ExpiredJwtException extends CustomException {
		public ExpiredJwtException(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}

	public static class NotFoundJwtException extends CustomException {
		public NotFoundJwtException(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}

	public static class UnsupportedJwtException extends CustomException {
		public UnsupportedJwtException(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}

	public static class IllegalArgumentException extends CustomException {
		public IllegalArgumentException(String message, ErrorCode errorCode) {
			super(message, errorCode);
		}
	}
}
