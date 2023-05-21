package com.tripbook.main.global.util;

import jakarta.servlet.http.HttpServletRequest;

public class CheckDevice {
	public static String checkDevice(HttpServletRequest request) {
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
