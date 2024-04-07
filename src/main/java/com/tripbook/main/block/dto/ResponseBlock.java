package com.tripbook.main.block.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBlock {

	@Builder
	@Getter
	@Schema(description = "차단에 대한 성공 응답 값")
	public static class ResultInfo {
		@Schema(description = "HTTP 상태값")
		private HttpStatus status;
		@Schema(description = "String배열 형태의 결과값")
		private List<String> message;
	}

	@Builder
	@Getter
	@Schema(description = "차단사용자목록")
	public static class BlockInfo {
		@Schema(description = "이메일")
		private String email;
		@Schema(description = "닉네임")
		private String name;
		@Schema(description = "프로필 URL")
		private String profile;
	}
}
