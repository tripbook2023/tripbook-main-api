package com.tripbook.main.auth.controller;

import com.tripbook.main.auth.dto.AuthResponse;
import com.tripbook.main.auth.service.LoginUserInfo;

public final class AuthDtoMapper {

	static AuthResponse of(LoginUserInfo info) {
		return new AuthResponse(
			info.nickname(),
			info.accessToken(),
			info.refreshToken(),
			info.status(),
			info.email()
		);
	}
}
