package com.tripbook.main.auth.filter;

import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.tripbook.main.auth.provider.AccessTokenAuthenticationProvider;
import com.tripbook.main.auth.token.CustomPlatformAccessToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuth2AccessTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX = "/login/oauth2/";
	private static final String HTTP_METHOD = "GET";
	private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";  //Header
	private static final AntPathRequestMatcher DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
		DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX + "*", HTTP_METHOD);

	public OAuth2AccessTokenAuthenticationFilter(AccessTokenAuthenticationProvider accessTokenAuthenticationProvider,
		AuthenticationSuccessHandler authenticationSuccessHandler,  //로그인 성공 시 처리
		AuthenticationFailureHandler authenticationFailureHandler) { //로그인 실패 시 처리
		super(DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER);
		this.setAuthenticationManager(new ProviderManager(accessTokenAuthenticationProvider));
		this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		this.setAuthenticationFailureHandler(authenticationFailureHandler);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		String accessToken = request.getHeader(ACCESS_TOKEN_HEADER_NAME);
		log.info("accessToken::", accessToken);
		//@TODO 세션 미사용으로 변경
		return this.getAuthenticationManager().authenticate(new CustomPlatformAccessToken(accessToken));
	}
}