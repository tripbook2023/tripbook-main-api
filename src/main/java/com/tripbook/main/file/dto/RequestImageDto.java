package com.tripbook.main.file.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestImageDto {
	ImageDto imageDto;

	@Getter
	@Setter
	@Schema(description = "이미지파일에 대한 DTO")
	public static class ImageDto implements Serializable {
		// 이미지 파일 용량 제한
		private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
		// 확장자 검사
		private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
		@Schema(title = "이미지 파일", example = "가능확장자 : jpg, jpeg, png, gif \n\n 파일용량 : 5MB")
		private MultipartFile imageFile;
		@JsonIgnore
		private boolean isDisable = false;

		public void setImageFile(MultipartFile imageFile) {
			//이미지파일 유효성 검사
			if (!isImageFileValid(imageFile)) {
				this.isDisable = true;
			}
			this.imageFile = imageFile;
		}

		public boolean isImageFileValid(MultipartFile imageFile) {
			if (imageFile == null || imageFile.isEmpty()) {
				return false;
			}

			if (imageFile.getSize() > MAX_FILE_SIZE) {
				return false;
			}

			String fileExtension = getFileExtension(imageFile.getOriginalFilename());
			if (fileExtension == null || !ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
				return false;
			}

			return true;
		}

		private String getFileExtension(String filename) {
			int dotIndex = filename.lastIndexOf(".");
			if (dotIndex > -1 && dotIndex < filename.length() - 1) {
				return filename.substring(dotIndex + 1);
			}
			return null;
		}
	}
}
