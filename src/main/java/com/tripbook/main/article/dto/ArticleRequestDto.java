package com.tripbook.main.article.dto;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tripbook.main.global.dto.LocationDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ArticleRequestDto {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Setter
	public static class ArticleSaveRequest {
		@Schema(description = "PK")
		private Long articleId;
		@Schema(description = "제목")
		@NotNull(message = "Title is required")
		private String title;

		@Schema(description = "내용")
		@NotNull(message = "Content is required")
		private String content;
		@Schema(description = "내용(본문)")
		@NotNull(message = "ContentOrigin is required")
		private String contentOrigin;
		@Schema(description = "이미지 ID리스트")
		private long[] fileIds;
		@Schema(description = "썸네일 ImageURL")
		private String thumbnail;
		// @Schema(description = "테그 리스트")
		// private List<String> tagList;
		@Schema(description = "위치태그 리스트")
		private List<LocationDto.LocationSimpleDto> locationList;

		// 확장자 검사
		private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
		@JsonIgnore
		private Boolean imageAccept = true;

		// public void setTagList(List<String> tagList) {
		// 	this.tagList = tagList;
		// }

		public void isImageFileValid(List<MultipartFile> imageFileList) {
			imageFileList.forEach(imageFile -> {

				String fileExtension = getFileExtension(imageFile.getOriginalFilename());
				if (fileExtension == null || !ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
					this.imageAccept = false;
				}
			});
		}

		public void isImageFileValid(MultipartFile image) {

			String fileExtension = getFileExtension(image.getOriginalFilename());
			if (fileExtension == null || !ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
				this.imageAccept = false;
			}
		}

		private String getFileExtension(String filename) {
			int dotIndex = filename.lastIndexOf(".");
			if (dotIndex > -1 && dotIndex < filename.length() - 1) {
				return filename.substring(dotIndex + 1);
			}
			return null;
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class CommentSaveRequest {

		@Schema(description = "댓글 내용")
		@NotNull(message = "Content is required")
		private String content;

		@Schema(description = "상위 댓글 ID")
		@NotNull(message = "parentId is required")
		private long parentId;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ReportRequest {

		@Schema(description = "신고 여행소식 ID")
		@NotNull(message = "ArticleId is required")
		private Long articleId;
		@Schema(description = "신고 여행소식 내용")
		@NotNull(message = "content is required")
		@Size(max = 30, message = "30자를 넘을 수 없습니다.")
		private String content;
	}
}
