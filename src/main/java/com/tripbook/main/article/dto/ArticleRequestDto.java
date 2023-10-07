package com.tripbook.main.article.dto;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
		@Schema(description = "제목")
		@NotNull(message = "Title is required")
		private String title;

		@Schema(description = "내용")
		@NotNull(message = "Content is required")
		private String content;

		@Schema(description = "이미지 리스트")
		private List<MultipartFile> imageList;
		@Schema(description = "썸네일 이미지")
		private MultipartFile thumbnail;

		@Schema(description = "테그 리스트")
		private List<String> tagList;
		private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
		// 확장자 검사
		private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
		@JsonIgnore
		private Boolean imageAccept = true;

		public void setImageList(List<MultipartFile> imageList) {
			this.imageList = imageList;
			isImageFileValid(imageList);
		}

		public void setThumbnail(MultipartFile thumbnail) {
			this.thumbnail = thumbnail;
			isImageFileValid(thumbnail);
		}

		public void setTagList(List<String> tagList) {
			this.tagList = tagList;
		}

		public void isImageFileValid(List<MultipartFile> imageFileList) {
			imageFileList.forEach(imageFile -> {
				if (imageFile.getSize() > MAX_FILE_SIZE || imageFile == null || imageFile.isEmpty()) {
					this.imageAccept = false;
				}
				String fileExtension = getFileExtension(imageFile.getOriginalFilename());
				if (fileExtension == null || !ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
					this.imageAccept = false;
				}
			});
		}

		public void isImageFileValid(MultipartFile image) {
			if (image.getSize() > MAX_FILE_SIZE || image == null || image.isEmpty()) {
				this.imageAccept = false;
			}
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

}
