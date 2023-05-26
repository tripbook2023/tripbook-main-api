package com.tripbook.main.auth.service;

import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberStatus;
import com.tripbook.main.token.dto.TokenInfo;

public record LoginUserInfo(
	String nickname,
	String accessToken,
	String refreshToken,
	MemberStatus status,
	String email
) {

	public LoginUserInfo(Member member, TokenInfo tokenInfo) {
		this(member.getName(), tokenInfo.getAccessToken(), tokenInfo.getRefreshToken(), member.getStatus(),
			member.getEmail());
	}
}
