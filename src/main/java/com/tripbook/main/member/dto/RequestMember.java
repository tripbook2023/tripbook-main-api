package com.tripbook.main.member.dto;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.tripbook.main.member.enums.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
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
	SignupMember signupMember;
	@Valid
	SignupSurvey signupSurvey;

	@Getter
	@Setter
	public static class SignupMember {
		@NotBlank(message = "name is required")
		@Size(min = 1, max = 9, message = "이름은 1 ~ 9자 이여야 합니다!")
		@Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "Nickname cannot contain special characters")
		@Schema(title = "닉네임")
		private String name;
		@Schema(title = "사용자 이메일", example = "lso5507@gmail.com")
		@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
		@NotNull(message = "email is required")
		private String email;
		@Schema(title = "프로필 이미지 URL", example = "https://IMAGEURL")
		private String profile;
		@Schema(title = "마케팅 수신 허용여부")
		@NotNull(message = "isMarketing is required")
		private Boolean isMarketing;
		@Schema(title = "성별", example = "MALE||FEMALE")
		@NotNull(message = "gender is required")
		private Gender gender;
		@Schema(title = "생일", description = "yyyy-mm-dd", type = "LocalDate", example = "1996-07-13")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date birth;
	}

	@Getter
	@Setter
	public static class SignupNameValidator {
		@NotBlank(message = "name is required")
		@Size(min = 1, max = 9, message = "이름은 1 ~ 9자 이여야 합니다!")
		@Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "Nickname cannot contain special characters")
		private String name;
	}

	@Getter
	@Setter
	public static class SignupSurvey {
		@NotBlank(message = "location is required")
		private String location;
		@NotBlank(message = "period is required")
		private String period;
		@NotBlank(message = "accompany is required")
		private String accompany;
		@NotBlank(message = "transportation is required")
		private String transportation;
		@NotBlank(message = "purpose is required")
		private String purpose;

	}

}
