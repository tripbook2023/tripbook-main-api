
package com.tripbook.main.security.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Author - 이석운 작성
 * SUccessHandler를 미리 선언해둠.
 * @TODO
 * 실제 로그인 완료 시 진행해야 할 로직 추가 필요.(햔재 임시로직)
 */
@Slf4j
@Component
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {

		if (exception instanceof BadCredentialsException) {
			// 사용 중지된 계정으로 로그인한 경우
			response.sendError(HttpStatus.CONFLICT.value(), "USER_ALREADY_exists:::");
		}
		log.error("Logind Failed");
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"유저정보 획득실패.");


	}
}