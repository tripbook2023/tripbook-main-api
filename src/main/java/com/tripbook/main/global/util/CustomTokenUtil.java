package com.tripbook.main.global.util;

import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public class CustomTokenUtil {
	// Request Header 에서 토큰 정보 추출
	public static String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
