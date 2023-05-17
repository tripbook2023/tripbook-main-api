package com.tripbook.main.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tripbook.main.auth.common.UserInfoRequest;
import com.tripbook.main.auth.token.CustomPlatformAccessToken;
import com.tripbook.main.member.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class LoadUserService {
	@Autowired
	private UserInfoRequest userInfoRequest;

	@SneakyThrows
	public Member getOAuth2UserDetails(CustomPlatformAccessToken authentication) {
		return userInfoRequest.getSocialEmail(authentication.getAccessToken());
	}

}
