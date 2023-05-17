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
	@ExceptionHandler(CustomException.EmailDuplicateException.class)
	public ResponseEntity<ErrorResponse> handleEmailDuplicateException(CustomException.EmailDuplicateException ex) {
		log.error("handleEmailDuplicateException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		log.error("handleException", ex);
		ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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

}
