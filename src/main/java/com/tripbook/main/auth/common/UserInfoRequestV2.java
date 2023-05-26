package com.tripbook.main.auth.common;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoRequestV2 {

	private final Auth0Client auth0Client;

	public Auth0UserInfo getUserInfoFromAuth0Token(final String token) {
		return auth0Client.getUserInfoFromAuth0Token("Bearer " + token);
	}
}
