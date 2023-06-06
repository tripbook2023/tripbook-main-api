package com.tripbook.main.file.dto;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class ResponseImageDto {
	@Builder
	@Getter
	@Schema(description = "이미지 업로드에대한 성공 응답 값")
	public static class ImageInfo {
		@Schema(description = "HTTP 상태값")
		private HttpStatus status;
		@Schema(description = "이미지URL")
		private String fileUrl;
	}
}
