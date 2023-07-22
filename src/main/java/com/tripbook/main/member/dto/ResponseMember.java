package com.tripbook.main.member.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.Gender;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResponseMember {

	@Builder
	@Getter
	@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값인 필드 제외
	@Schema(description = "회원가입 성공에 대한 응답 값")
	public static class Info {
		@Schema(title = "JWT_AccessToken")
		private String accessToken;
		@Schema(title = "JWT_refreshToken")
		private String refreshToken;
		@Schema(title = "성공여부")
		private String message;
	}

	@Builder
	@Getter
	@Schema(description = "멤버에 대한 성공 응답 값")
	public static class ResultInfo {
		@Schema(description = "HTTP 상태값")
		private HttpStatus status;
		@Schema(description = "String배열 형태의 결과값")
		private List<String> message;
	}

	@Getter
	@Schema(description = "멤버조회에 대한 응답 값")
	public static class MemberInfo {
		public MemberInfo(Member member) {
			this.email = member.getEmail();
			this.name = member.getName();
			this.gender = member.getGender();
			this.role = member.getRole();
			this.birth = member.getBirth();
			this.profile = member.getProfile();
			this.termsOfService = member.getTermsOfService();
			this.termsOfPrivacy = member.getTermsOfPrivacy();
			this.termsOfLocation = member.getTermsOfLocation();
			this.marketingConsent = member.getMarketingConsent();
			this.point = member.getPoint();
			this.status = member.getStatus();
		}

		@Schema(description = "이메일")
		private String email;
		@Schema(description = "닉네임")
		private String name;
		@Schema(description = "성별")
		private Gender gender;
		@Schema(description = "멤버권한")
		private MemberRole role;
		@Schema(description = "생년월일")
		private LocalDate birth;
		@Schema(description = "프로필URL")
		private String profile;
		@Schema(description = "서비스이용동의여부")
		private Boolean termsOfService;
		@Schema(description = "개인정보처리")
		private Boolean termsOfPrivacy;
		@Schema(description = "위치정보수집동의")
		private Boolean termsOfLocation;
		@Schema(description = "마케팅이용동의")
		private Boolean marketingConsent;
		@Schema(description = "포인트")
		private Long point;
		@Schema(description = "계정상태")
		private MemberStatus status;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class MemberSimpleDto {
		private long id;
		private String name;
		private String profileUrl;
		private MemberRole role;
	}

}
