package com.tripbook.main.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMember {
	SignupMember signupMember;
	SignupSurvey signupSurvey;

	@Getter
	@Setter
	public static class SignupMember {
		private String email;
		private String name;
		private String profile;
		private Boolean isMarketing;
	}

	@Getter
	@Setter
	public static class SignupSurvey {
		private String location;
		private String period;
		private String accompany;
		private String transportation;
		private String purpose;

	}

}
