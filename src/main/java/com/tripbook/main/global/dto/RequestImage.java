package com.tripbook.main.global.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.tripbook.main.global.entity.Image;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestImage {
	@Getter
	@Setter
	@Schema(description = "이미지정보 대한 요청 값")
	public static class ImageInfo {
		@Schema(description = "파일IDs")
		private List<Long>fileIds;



	}
}
