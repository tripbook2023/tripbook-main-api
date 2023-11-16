package com.tripbook.main.auth.dto;

import java.io.Serializable;

import com.tripbook.main.member.enums.MemberStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class ResponseAuth {
	@Builder
	@Getter
	@Schema(description = "간편로그인에 대한 성공 응답 값")
	public static class ResultData implements Serializable {
		private static final long serialVersionUID = 15623L;
		@Schema(title = "사용자 닉네임")
		private String nickname;
		@Schema(title = "액세스토큰(JWT)")
		private String accessToken;
		@Schema(title = "리프레시토큰(JWT)")
		private String refreshToken;
		@Schema(title = "계정상태", description = "1.STATUS_NORMAL-정상계정\n\n 2.STATUS_SUSPEND-정지계정\n\n 3.STATUS_WITHDRAWAL-탈퇴계정\n\n4.STATUS_SLEEP-휴먼계정\n\n5.STATUS_REQUIRED_AUTH-회원가입 필요")
		private MemberStatus status;
		@Schema(title = "이메일")
		private String email;
	}
}
