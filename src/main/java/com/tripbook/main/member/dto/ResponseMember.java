package com.tripbook.main.member.dto;

import com.tripbook.main.member.enums.Gender;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMember {

	@Builder
	@Getter
	public static class Info {
		private String name;
		private String email;
		private Gender gender;
		private MemberRole role;
		private String accessToken;
		private String refreshToken;
		private MemberStatus status;
	}

	@Builder
	@Getter
	public static class resultInfo {
		private String result;
	}

}
