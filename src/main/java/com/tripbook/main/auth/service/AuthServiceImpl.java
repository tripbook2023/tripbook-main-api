package com.tripbook.main.auth.service;

import org.springframework.stereotype.Service;

import com.tripbook.main.auth.common.Auth0UserInfo;
import com.tripbook.main.auth.common.UserInfoRequestV2;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;
import com.tripbook.main.member.repository.MemberRepository;
import com.tripbook.main.token.dto.TokenInfo;
import com.tripbook.main.token.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserInfoRequestV2 userInfoRequestV2;
	private final MemberRepository memberRepository;
	private final JwtService jwtService;

	@Override
	public LoginUserInfo login(final String accessToken) {
		final Auth0UserInfo userInfo = userInfoRequestV2.getUserInfoFromAuth0Token(accessToken);
		final Member member = memberRepository.findOptionalByEmail(userInfo.email())
			.orElse(createNewMemberFrom(userInfo));
		final TokenInfo tokenInfo = jwtService.saveToken(member, "WEB");

		return new LoginUserInfo(member, tokenInfo);
	}

	private Member createNewMemberFrom(final Auth0UserInfo userInfo) {
		return memberRepository.save(Member.builder()
			.email(userInfo.email())
			.name(userInfo.name())
			.role(MemberRole.ROLE_MEMBER)
			.status(MemberStatus.ADDITIONAL_AUTHENTICATION)
			.isMarketing(false)
			.build()
		);
	}
}
