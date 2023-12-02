package com.tripbook.main.global.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.tripbook.main.global.entity.Image;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.Gender;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseImage {
	@Builder
	@Getter
	@Schema(description = "이미지업로드에 대한 성공 응답 값")
	public static class ResultInfo {
		@Schema(description = "HTTP 상태값")
		private HttpStatus status;
		@Schema(description = "String배열 형태의 결과값")
		private List<String> message;
	}
	@Getter
	@Schema(description = "이미지정보 대한 응답 값")
	public static class ImageInfo {
		@Builder
		public ImageInfo(Image image) {
			this.id = image.getId();
			this.url = image.getUrl();
			this.name = image.getName();
			this.refId = image.getRefId();
			this.refType = image.getRefType();
		}

		@Schema(description = "ID")
		private long id;
		@Schema(description = "파일URL")
		private String url;
		@Schema(description = "파일이름")
		private String name;
		@Schema(description = "파일RefId")
		private long refId;
		@Schema(description = "파일카테고리")
		private String refType;


	}
}
