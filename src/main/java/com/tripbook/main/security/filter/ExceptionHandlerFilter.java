package com.tripbook.main.security.filter;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
			setErrorResponse(response, ErrorCode.EMAIL_DUPLICATION);
		} catch (CustomException.InvalidTokenException e) {
			setErrorResponse(response, ErrorCode.TOKEN_UNAUTHORIZED);
		} catch (CustomException.AuthenticationException | AuthenticationException e) {
			setErrorResponse(response, ErrorCode.FILTER_UNAUTHORIZED);
		} catch (CustomException.AccessDeniedException | AccessDeniedException e) {
			setErrorResponse(response, ErrorCode.FILTER_ACCESS_DENIED);
		} catch (CustomException.CsrfException e) {
			setErrorResponse(response, ErrorCode.FILTER_CSRF_ERROR);
		} catch (CustomException.SecurityException e) {
			setErrorResponse(response, ErrorCode.JWT_INVALID_ERROR);
		} catch (CustomException.ExpiredJwtException e) {
			setErrorResponse(response, ErrorCode.JWT_EXPIRED_ERROR);
		} catch (CustomException.UnsupportedJwtException e) {
			setErrorResponse(response, ErrorCode.JWT_UNSUPPORTED_ERROR);
		} catch (CustomException.IllegalArgumentException e) {
			setErrorResponse(response, ErrorCode.JWT_EMPTY_ERROR);
		} catch (CustomException.UnsupportedPlatform e) {
			setErrorResponse(response, ErrorCode.JWT_UNSUPPORTED_PLATFORM_ERROR);
		}
	}

	private void setErrorResponse(
		HttpServletResponse response,
		ErrorCode errorCode
	) {
		ObjectMapper objectMapper = new ObjectMapper();
		response.setStatus(errorCode.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ErrorResponse errorResponse = new ErrorResponse(errorCode);
		try {
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}