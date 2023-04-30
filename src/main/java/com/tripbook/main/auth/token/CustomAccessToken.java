package com.tripbook.main.auth.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Builder;

public class CustomAccessToken extends AbstractAuthenticationToken {
	private Object principal;
	private String accessToken;

	public CustomAccessToken(String accessToken) {
		super(null);
		this.accessToken = accessToken;
		setAuthenticated(false);
	}

	@Builder
	public CustomAccessToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
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