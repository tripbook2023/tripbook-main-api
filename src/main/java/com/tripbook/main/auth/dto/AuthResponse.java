package com.tripbook.main.auth.dto;

import com.tripbook.main.member.enums.MemberStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
	@Schema(title = "사용자 닉네임")
	String nickname,
	@Schema(title = "액세스토큰(JWT)")
	String accessToken,
	@Schema(title = "리프레시토큰(JWT)")
	String refreshToken,
	@Schema(title = "계정상태", description = "1.STATUS_NORMAL-정상계정\n\n 2.STATUS_SUSPEND-정지계정\n\n 3.STATUS_WITHDRAWAL-탈퇴계정\n\n4.STATUS_SLEEP-휴먼계정")
	MemberStatus status,
	@Schema(title = "이메일")
	String email
) {
}
