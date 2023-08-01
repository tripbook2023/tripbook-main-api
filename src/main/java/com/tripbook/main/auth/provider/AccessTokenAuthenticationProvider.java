package com.tripbook.main.auth.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.tripbook.main.auth.dto.ResponseAuth.ResultInfo;
import com.tripbook.main.auth.service.LoadUserService;
import com.tripbook.main.auth.token.CustomPlatformAccessToken;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {
	private final LoadUserService loadUserService;

	@SneakyThrows
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		ResultInfo resultInfo = loadUserService.getOAuth2UserDetails(
			(CustomPlatformAccessToken)authentication);
		return new CustomPlatformAccessToken(resultInfo);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return CustomPlatformAccessToken.class.isAssignableFrom(
			authentication);
	}

}
