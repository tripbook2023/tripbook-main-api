package com.tripbook.main.auth.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	@Override
	public LoginUserInfo login(final String accessToken) {
		throw new UnsupportedOperationException("Not Implemented");
	}
}
