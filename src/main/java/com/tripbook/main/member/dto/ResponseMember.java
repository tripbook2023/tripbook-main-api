package com.tripbook.main.member.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.tripbook.main.member.enums.Gender;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;

import io.swagger.v3.oas.annotations.media.Schema;
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
	@Schema(description = "멤버에 대한 성공 응답 값")
	public static class resultInfo {
		@Schema(description = "HTTP 상태값")
		private HttpStatus status;
		@Schema(description = "String배열 형태의 결과값")
		private List<String> message;
	}

}
