package com.tripbook.main.member.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMember {

	@Builder
	@Getter
	@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값인 필드 제외
	@Schema(description = "회원가입 성공에 대한 응답 값")
	public static class Info {
		// @Schema(title = "HTTP 상태값")
		// private String name;
		// @Schema(title = "사용자 이메일")
		// private String email;
		// @Schema(title = "사용자 성별")
		// private Gender gender;
		// @Schema(title = "사용자 권한")
		// private MemberRole role;
		@Schema(title = "JWT_AccessToken")
		private String accessToken;
		@Schema(title = "JWT_refreshToken")
		private String refreshToken;
		// @Schema(title = "사용자 계정상태")
		// private MemberStatus status;
		@Schema(title = "성공여부")
		private String message;
	}

	@Builder
	@Getter
	@Schema(description = "멤버에 대한 성공 응답 값")
	public static class ResultInfo {
		@Schema(description = "HTTP 상태값")
		private HttpStatus status;
		@Schema(description = "String배열 형태의 결과값")
		private List<String> message;
	}

}
