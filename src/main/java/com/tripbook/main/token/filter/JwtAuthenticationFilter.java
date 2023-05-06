package com.tripbook.main.token.filter;

import java.util.Collections;
import java.util.regex.Pattern;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.tripbook.main.member.dto.PrincipalMemberDto;
import com.tripbook.main.token.provider.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtProvider jwtProvider;

	@Override
	@SneakyThrows
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		// 1. Request Header 에서 JWT 토큰 추출
		String token = resolveToken(httpRequest);
		if (requiresAuthentication(httpRequest.getRequestURI())) {
			if (StringUtils.hasText(token)) {
				OAuth2AuthenticationToken authentication = getAuthentication(token);
				if (authentication != null) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		chain.doFilter(request, response);
	}

	// Access Token으로 OAuth2AuthenticationToken 생성
	private OAuth2AuthenticationToken getAuthentication(String accessToken) throws Exception {
		PrincipalMemberDto principalMemberDto = jwtProvider.getAuthentication(accessToken);
		OAuth2User principal = new DefaultOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(principalMemberDto.getRole().toString())),
			Collections.singletonMap("email", principalMemberDto.getEmail()),
			"email");
		OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(principal,
			Collections.singleton(new SimpleGrantedAuthority(principalMemberDto.getRole().toString())),
			"auth0");
		return authentication;
	}

	// Request Header 에서 토큰 정보 추출
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	// 권한이 필요한 URI 패턴
	private static final Pattern AUTH_REQUIRED_URI_PATTERN = Pattern.compile("^/member/.*$");

	// 권한이 필요한 URI 패턴인지 확인하는 메소드
	private boolean requiresAuthentication(String requestURI) {
		return AUTH_REQUIRED_URI_PATTERN.matcher(requestURI).matches();
	}
}