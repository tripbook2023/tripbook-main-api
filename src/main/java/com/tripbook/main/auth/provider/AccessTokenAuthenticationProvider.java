package com.tripbook.main.auth.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.tripbook.main.auth.service.LoadUserService;
import com.tripbook.main.auth.token.CustomPlatformAccessToken;
import com.tripbook.main.member.dto.ResponseMember;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.service.MemberService;
import com.tripbook.main.token.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {
	private final LoadUserService loadUserService;
	private final MemberService memberService;
	private final JwtService jwtService;

	@SneakyThrows
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Member tmpMember = loadUserService.getOAuth2UserDetails((CustomPlatformAccessToken)authentication);
		//유저 저장
		Member resultMember = saveMember(tmpMember);
		//JWT 토큰 생성
		String accessToken = savaJwt(resultMember, ((CustomPlatformAccessToken)authentication).getDevice());
		return CustomPlatformAccessToken.builder()
			.principal(ResponseMember.Info.builder()
				.accessToken(accessToken)
				.email(resultMember.getEmail())
				.name(resultMember.getName())
				.status(resultMember.getStatus())
				.build())
			.build();
	}

	private String savaJwt(Member saveMember, String deviceType) {
		return jwtService.saveToken(saveMember, deviceType);
	}

	private Member saveMember(Member member) {
		return memberService.memberSave(member);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return CustomPlatformAccessToken.class.isAssignableFrom(
			authentication);
	}

}
