package com.tripbook.main.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tripbook.main.auth.common.UserInfoRequest;
import com.tripbook.main.auth.token.CustomAccessToken;
import com.tripbook.main.auth.userdetails.OAuth2UserDetails;
import com.tripbook.main.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoadUserService {
	@Autowired
	private UserInfoRequest userInfoRequest;

	public OAuth2UserDetails getOAuth2UserDetails(CustomAccessToken authentication) {
		Member member = userInfoRequest.getSocialEmail(authentication.getAccessToken());
		return OAuth2UserDetails.builder()
			.email(member.getEmail())
			.username(member.getName())
			.build();
	}

}
