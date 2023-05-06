package com.tripbook.main.auth.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import lombok.Builder;

public class CustomPlatformAccessToken extends AbstractAuthenticationToken {
	private Object principal;
	private String accessToken;

	public CustomPlatformAccessToken(String accessToken) {
		super(null);
		this.accessToken = accessToken;
		setAuthenticated(false);
	}

	@Builder
	public CustomPlatformAccessToken(Object principal) {
		super(null);
		this.principal = principal;
		super.setAuthenticated(true); // must use super, as we override
	}

	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

}