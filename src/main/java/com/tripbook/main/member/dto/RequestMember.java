package com.tripbook.main.member.dto;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.tripbook.main.member.enums.Gender;

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
		private String name;
		private String profile;
		@NotNull(message = "isMarketing is required")
		private Boolean isMarketing;
		@NotNull(message = "gender is required")
		private Gender gender;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date birth;
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
