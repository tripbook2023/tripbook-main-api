package com.tripbook.main.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tripbook.main.auth.common.UserInfoRequest;
import com.tripbook.main.auth.token.CustomAccessToken;
import com.tripbook.main.auth.userdetails.OAuth2UserDetails;
import com.tripbook.main.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoadUserService {

	public OAuth2UserDetails getOAuth2UserDetails(CustomAccessToken authentication)  {
		Member member = UserInfoRequest.getSocialEmail(authentication.getAccessToken());
		return OAuth2UserDetails.builder()
			.email(member.getEmail())
			.username(member.getName())
			.build();
	}


}
