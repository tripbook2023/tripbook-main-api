package com.tripbook.main.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tripbook.main.auth.dto.ResponseAuth;
import com.tripbook.main.global.util.CustomJsonUtil;
import com.tripbook.main.member.dto.ResponseMember;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Author - 이석운 작성
 * SUccessHandler를 미리 선언해둠.
 *
 * @TODO 실제 로그인 완료 시 진행해야 할 로직 추가 필요.(햔재 임시로직)
 */
@Slf4j
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication auth) throws IOException {
		ResponseMember.Info memberInfo = (ResponseMember.Info)auth.getPrincipal();
		ResponseAuth.resultInfo resultInfo = ResponseAuth.resultInfo.builder()
			.nickname(memberInfo.getName())
			.email(memberInfo.getEmail())
			.accessToken(memberInfo.getAccessToken())
			.refreshToken(memberInfo.getRefreshToken())
			.status(memberInfo.getStatus())
			.build();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(CustomJsonUtil.ObjectToString(resultInfo));
		response.setStatus(HttpServletResponse.SC_OK);
	}
}