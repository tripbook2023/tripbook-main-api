package com.tripbook.main.global.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
	//----------JWT Exceptions
	@ExceptionHandler(CustomException.SecurityException.class)
	public ResponseEntity<ErrorResponse> handleSecurityException(CustomException.SecurityException ex) {
		log.error("SecurityException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(CustomException.NotFoundJwtException.class)
	public ResponseEntity<ErrorResponse> handleTokenNotFoundException(CustomException.NotFoundJwtException ex) {
		log.error("NotFoundJwtException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(CustomException.ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> handleExpiredJwtException(CustomException.ExpiredJwtException ex) {
		log.error("ExpiredJwtException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(CustomException.UnsupportedJwtException.class)
	public ResponseEntity<ErrorResponse> handleUnsupportedJwtException(CustomException.UnsupportedJwtException ex) {
		log.error("UnsupportedJwtException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(CustomException.IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(CustomException.IllegalArgumentException ex) {
		log.error("IllegalArgumentException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	// -- JWT Exceptions END
	@ExceptionHandler(CustomException.EmailDuplicateException.class)
	public ResponseEntity<ErrorResponse> handleEmailDuplicateException(CustomException.EmailDuplicateException ex) {
		log.error("handleEmailDuplicateException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		//@TODO ErrorReponse 메시지 LIST 형식으로 변경
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getDefaultMessage())
			.collect(Collectors.toList());
		ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		log.error("handleException", ex);
		ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
