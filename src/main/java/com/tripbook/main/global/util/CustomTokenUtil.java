package com.tripbook.main.global.util;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public class CustomTokenUtil {

	private final static String TOKEN_PREFIX = "Bearer";

	// Request Header 에서 토큰 정보 추출
	public static String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
			return bearerToken.substring(TOKEN_PREFIX.length() + 1);
		}
		return null;
	}
}
