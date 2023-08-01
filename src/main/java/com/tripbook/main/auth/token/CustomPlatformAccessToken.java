package com.tripbook.main.auth.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomPlatformAccessToken extends AbstractAuthenticationToken {
	private Object principal;
	private String accessToken;
	private String device;

	public CustomPlatformAccessToken(String accessToken, String device) {
		super(null);
		this.accessToken = accessToken;
		this.device = device;
		setAuthenticated(false);
	}

	@Builder
	public CustomPlatformAccessToken(Object principal) {
		super(null);
		this.principal = principal;
		super.setAuthenticated(true); // must use super, as we override
	}

	@Override
	public Object getCredentials() {
		return null;
	}

}