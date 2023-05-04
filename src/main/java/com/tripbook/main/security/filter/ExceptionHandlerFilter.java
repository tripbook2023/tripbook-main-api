package com.tripbook.main.security.filter;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (CustomException.EmailDuplicateException e) {
			//이메일 중복검사
			setErrorResponse(response, ErrorCode.EMAIL_DUPLICATION);
		} catch (CustomException.InvalidTokenException e) {
			setErrorResponse(response, ErrorCode.TOKEN_Unauthorized);
		}
	}

	private void setErrorResponse(
		HttpServletResponse response,
		ErrorCode errorCode
	) {
		ObjectMapper objectMapper = new ObjectMapper();
		response.setStatus(ErrorCode.EMAIL_DUPLICATION.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());
		try {
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Data
	public static class ErrorResponse {
		private final Integer code;
		private final String message;
	}
}