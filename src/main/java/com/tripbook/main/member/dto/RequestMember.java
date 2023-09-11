package com.tripbook.main.member.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tripbook.main.member.enums.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMember {

	@Valid
	MemberReqInfo memberInfo;

	@Getter
	@Setter
	public static class MemberReqInfo {
		@NotBlank(message = "name is required")
		@Size(min = 1, max = 9, message = "이름은 1 ~ 9자 이여야 합니다!")
		@Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "Nickname cannot contain special characters")
		@Schema(title = "닉네임")
		private String name;
		@Schema(title = "사용자 이메일", example = "lso5507@gmail.com")
		@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
		@NotNull(message = "email is required")
		private String email;
		@Schema(title = "이미지 파일", example = "가능확장자 : jpg, jpeg, png, gif \n\n 파일용량 : 5MB")
		@Nullable
		private MultipartFile imageFile;
		@Schema(title = "서비스 이용 약관 동의 여부")
		@NotNull(message = "termsOfService is required")
		private Boolean termsOfService;
		@Schema(title = "개인정보 수집 및 이용 동의 여부")
		@NotNull(message = "termsOfPrivacy is required")
		private Boolean termsOfPrivacy;
		@Schema(title = "위치정보 수집 및 이용 동의 여부")
		@NotNull(message = "termsOfLocation is required")
		private Boolean termsOfLocation;
		@Schema(title = "마케팅 수신 허용여부")
		@NotNull(message = "marketingConsent is required")
		private Boolean marketingConsent;
		@Schema(title = "성별")
		@NotNull(message = "gender is required")
		private Gender gender;
		@Schema(title = "생일", description = "yyyy-mm-dd", type = "LocalDate", example = "1996-07-13")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate birth;
		private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
		// 확장자 검사
		private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

		public void setImageFile(MultipartFile imageFile) {
			//이미지파일 유효성 검사
			if (!isImageFileValid(imageFile)) {
				this.imageFile = null;
			}
			this.imageFile = imageFile;
		}

		public boolean isImageFileValid(MultipartFile imageFile) {
			if (imageFile.getSize() > MAX_FILE_SIZE || imageFile == null || imageFile.isEmpty()) {
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

	@Getter
	@Setter
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class UpdateProfile {
		@Size(min = 1, max = 9, message = "이름은 1 ~ 9자 이여야 합니다!")
		@Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "Nickname cannot contain special characters")
		@Schema(title = "닉네임")
		private String name;
		@Schema(title = "이미지 파일", example = "가능확장자 : jpg, jpeg, png, gif \n\n 파일용량 : 5MB")
		@Nullable
		private MultipartFile imageFile;
		@Schema(title = "서비스 이용 약관 동의 여부")
		private Boolean termsOfService;
		@Schema(title = "개인정보 수집 및 이용 동의 여부")
		private Boolean termsOfPrivacy;
		@Schema(title = "위치정보 수집 및 이용 동의 여부")
		private Boolean termsOfLocation;
		@Schema(title = "마케팅 수신 허용여부")
		private Boolean marketingConsent;
		@Schema(title = "성별")
		private Gender gender;
		@Schema(title = "생일", description = "yyyy-mm-dd", type = "LocalDate", example = "1996-07-13")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate birth;
		private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
		// 확장자 검사
		private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

		public void setImageFile(MultipartFile imageFile) {
			//이미지파일 유효성 검사
			if (!isImageFileValid(imageFile)) {
				this.imageFile = null;
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

	@Getter
	@Setter
	public static class SignupNameValidator {
		@NotBlank(message = "name is required")
		@Size(min = 1, max = 9, message = "이름은 1 ~ 9자 이여야 합니다!")
		@Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "Nickname cannot contain special characters")
		private String name;
	}

}
