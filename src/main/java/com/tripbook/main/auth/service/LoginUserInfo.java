package com.tripbook.main.auth.service;

import com.tripbook.main.member.enums.MemberStatus;

public record LoginUserInfo(
	String nickname,
	String accessToken,
	String refreshToken,
	MemberStatus status,
	String email
) {
}
