package com.tripbook.main.auth.filter;

import java.io.IOException;

import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.tripbook.main.auth.provider.AccessTokenAuthenticationProvider;
import com.tripbook.main.auth.token.CustomAccessToken;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuth2AccessTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX = "/login/oauth2/";  // /login/oauth2/ + ????? 로 오는 요청을 처리할 것이다

	private static final String HTTP_METHOD = "GET";    //HTTP 메서드의 방식은 GET이다.

	private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";  //AccessToken을 해더에 보낼 때, 해더의 key는 Authorization이다.


	private static final AntPathRequestMatcher DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER =
		new AntPathRequestMatcher(DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX +"*", HTTP_METHOD); //=>   /oauth2/login/* 의 요청에, GET으로 온 요청에 매칭된다.

	public OAuth2AccessTokenAuthenticationFilter(AccessTokenAuthenticationProvider accessTokenAuthenticationProvider,   //Provider를 등록해주었다. 이는 조금 이따 설명하겠다.
		AuthenticationSuccessHandler authenticationSuccessHandler,  //로그인 성공 시 처리할  handler이다
		AuthenticationFailureHandler authenticationFailureHandler) { //로그인 실패 시 처리할 handler이다.

		super(DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER);   // 위에서 설정한  /oauth2/login/* 의 요청에, GET으로 온 요청을 처리하기 위해 설정한다.

		this.setAuthenticationManager(new ProviderManager(accessTokenAuthenticationProvider));
		//AbstractAuthenticationProcessingFilter를 커스터마이징 하려면  ProviderManager를 꼭 지정해 주어야 한다(안그러면 예외남!!!)

		this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		this.setAuthenticationFailureHandler(authenticationFailureHandler);

	}



	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException, IOException, ServletException {
		String accessToken = request.getHeader(ACCESS_TOKEN_HEADER_NAME); //헤더의 AccessToken에 해당하는 값을 가져온다.
		log.info("accessToken::",accessToken);
		return this.getAuthenticationManager().authenticate(new CustomAccessToken(accessToken));
		//AuthenticationManager에게 인증 요청을 보낸다. 이때 Authentication 객체로는 AccessTokenSocialTypeToken을(직접 커스텀 함) 사용한다.
	}
}