package com.tripbook.main.auth.filter;

import static com.tripbook.main.global.util.CustomTokenUtil.*;

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
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuth2AccessTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX = "/login/oauth2/";
	private static final String HTTP_METHOD = "GET";
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
		String accessToken = resolveToken(request);
		if (accessToken == null) {
			throw new CustomException.InvalidTokenException(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(),
				ErrorCode.TOKEN_UNAUTHORIZED);
		}
		return this.getAuthenticationManager()
			.authenticate(new CustomPlatformAccessToken(accessToken, checkDevice(request)));
	}

	private String checkDevice(HttpServletRequest request) {
		// 운영체제 정보
		String userAgent = request.getHeader("User-Agent");

		// 모바일 기종 체크
		boolean isMobile = userAgent.matches(
			".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*");

		// IOS_APP, ANDROID_APP 앱 특정 변수(변동)
		if (userAgent.indexOf("IOS_APP") > -1 || userAgent.indexOf("ANDROID_APP") > -1) {
			return "APP";
		} else if (isMobile) {
			return "MOBILE";
		} else {
			return "WEB";
		}
	}
}