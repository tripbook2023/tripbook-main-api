package com.tripbook.main.global.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.util.discord.DiscordAlarm;

import lombok.extern.slf4j.Slf4j;

//@TODO - 가독성 향상을 위한 Handler 도메인 별 관리
@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
	//----------JWT Exceptions
	@DiscordAlarm
	@ExceptionHandler(CustomException.SecurityException.class)
	public ResponseEntity<ErrorResponse> handleSecurityException(CustomException.SecurityException ex) {
		log.error("SecurityException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@DiscordAlarm
	@ExceptionHandler(CustomException.UnsupportedImageFileException.class)
	public ResponseEntity<ErrorResponse> UnsupportedImageFileException(
		CustomException.UnsupportedImageFileException ex) {
		log.info("UnsupportedImageFileException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@DiscordAlarm
	@ExceptionHandler(CustomException.NotFoundJwtException.class)
	public ResponseEntity<ErrorResponse> handleTokenNotFoundException(CustomException.NotFoundJwtException ex) {
		log.error("NotFoundJwtException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@DiscordAlarm
	@ExceptionHandler(CustomException.ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> handleExpiredJwtException(CustomException.ExpiredJwtException ex) {
		log.info("ExpiredJwtException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@DiscordAlarm
	@ExceptionHandler(CustomException.UnsupportedJwtException.class)
	public ResponseEntity<ErrorResponse> handleUnsupportedJwtException(CustomException.UnsupportedJwtException ex) {
		log.error("UnsupportedJwtException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@DiscordAlarm
	@ExceptionHandler(CustomException.IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(CustomException.IllegalArgumentException ex) {
		log.error("IllegalArgumentException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	// -- JWT Exceptions END
	// -- Member Exceptions
	@DiscordAlarm
	@ExceptionHandler(CustomException.MemberAlreadyExist.class)
	public ResponseEntity<ErrorResponse> MemberAlreadyExist(CustomException.MemberAlreadyExist ex) {
		log.info("MemberAlreadyExist", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(CustomException.MemberNameAlreadyException.class)
	public ResponseEntity<ErrorResponse> MemberNameAlreadyException(CustomException.MemberNameAlreadyException ex) {
		log.info("MemberNameAlreadyExist", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@DiscordAlarm
	@ExceptionHandler(CustomException.MemberAlreadyAuthenticate.class)
	public ResponseEntity<ErrorResponse> MemberAlreadyAuthenticate(CustomException.MemberAlreadyAuthenticate ex) {
		log.info("MemberAlreadyAuthenticate", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(CustomException.MemberNotFound.class)
	public ResponseEntity<ErrorResponse> MemberNotFound(CustomException.MemberNotFound ex) {
		log.info("MemberNotFound", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@DiscordAlarm
	@ExceptionHandler(CustomException.EmailDuplicateException.class)
	public ResponseEntity<ErrorResponse> handleEmailDuplicateException(CustomException.EmailDuplicateException ex) {
		log.info("handleEmailDuplicateException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}
	// -- Member Exceptions

	@DiscordAlarm
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		//@TODO ErrorReponse 메시지 LIST 형식으로 변경
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getDefaultMessage())
			.collect(Collectors.toList());
		ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST);
		response.setMessage(errors);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@DiscordAlarm
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		log.error("handleException", ex);
		ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DiscordAlarm
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> MultipartHandleException(Exception ex) {
		log.error("handleException", ex);
		ErrorResponse response = new ErrorResponse(ErrorCode.FILE_MAXIMUM_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(CustomException.MemberNotPermittedException.class)
	public ResponseEntity<ErrorResponse> handleMemberNotPermittedException(Exception ex) {
		log.info("handleException", ex);
		ErrorResponse response = new ErrorResponse(ErrorCode.MEMBER_NOT_PERMITTED);
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(CustomException.ArticleNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleArticleNotFoundException(Exception ex) {
		log.info("handleException", ex);
		ErrorResponse response = new ErrorResponse(ErrorCode.ARTICLE_NOT_FOUND);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CustomException.CommentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCommentNotFoundException(Exception ex) {
		log.info("handleException", ex);
		ErrorResponse response = new ErrorResponse(ErrorCode.COMMENT_NOT_FOUND);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(CustomException.CommonUnSupportedException.class)
	@DiscordAlarm
	public ResponseEntity<ErrorResponse> handleCommonUnSupportedException(CustomException.CommonUnSupportedException ex) {
		log.info("handleException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response,  HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}
	@ExceptionHandler(CustomException.CommonNotPermittedException.class)
	@DiscordAlarm
	public ResponseEntity<ErrorResponse> handleCommonNotPermittedException(CustomException.CommonNotPermittedException ex) {
		log.info("handleException", ex);
		ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}


}
