package com.tripbook.main.article.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tripbook.main.member.dto.ResponseMember;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ArticleResponseDto {
	@Builder
	@Getter
	@Schema(description = "여행소식에 대한 성공 응답 값")
	public static class ResultInfo {
		@Schema(description = "HTTP 상태값")
		private HttpStatus status;
		@Schema(description = "String배열 형태의 결과값")
		private List<String> message;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ArticleResponse {
		@Schema(description = "여행소식 ID")
		private long id;

		@Schema(description = "제목")
		private String title;

		@Schema(description = "내용")
		private String content;

		@Schema(description = "작성자")
		private ResponseMember.MemberSimpleDto author;

		@Schema(description = "썸네일이미지")
		private String thumbnailUrl;

		@Schema(description = "태그 목록")
		private List<String> tagList;

		@Schema(description = "좋아요 수")
		private long heartNum;

		@Schema(description = "좋아요 여부")
		private boolean isHeart;

		@Schema(description = "북마크 수")
		private long bookmarkNum;

		@Schema(description = "북마크 여부")
		private boolean isBookmark;
		@Schema(description = "장소")
		private List<LocationResponse> location;

		@Schema(description = "댓글 목록")
		private List<CommentResponse> commentList;

		@Schema(description = "생성일")
		private LocalDateTime createdAt;

		@Schema(description = "수정일")
		private LocalDateTime updatedAt;

	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class CommentResponse {
		@Schema(description = "댓글 ID")
		private long id;

		@Schema(description = "내용")
		private String content;

		@Schema(description = "작성자")
		private ResponseMember.MemberSimpleDto author;

		@Schema(description = "하위 댓글")
		private List<CommentResponse> childList;

		@Schema(description = "생성일")
		private LocalDateTime createdAt;

		@Schema(description = "수정일")
		private LocalDateTime updatedAt;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class LocationResponse {
		@Schema(description = "여행장소 ID")
		private long id;

		@Schema(description = "X")
		private String locationX;
		@Schema(description = "Y")
		private String locationY;
		@Schema(description = "여행장소 이름")
		private String name;

	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ImageResponse {
		@Schema(description = "이미지 ID")
		private long id;

		@Schema(description = "이미지 URL")
		private String url;
	}

}
