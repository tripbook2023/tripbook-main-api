package com.tripbook.main.member.vo;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.tripbook.main.member.enums.Gender;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
public class MemberVO {
	private String name;
	private String email;
	private Boolean termsOfService;
	private Boolean termsOfPrivacy;
	private Boolean termsOfLocation;
	private Boolean marketingConsent;
	private Gender gender;
	private MultipartFile imageFile;
	@Setter
	private String profile;

	private MemberRole role;
	private LocalDate birth;
	private MemberStatus status;

	@Builder
	public MemberVO(String name, MemberRole role, String email, Boolean termsOfService, Boolean termsOfPrivacy,
		Boolean termsOfLocation,
		Boolean marketingContent, Gender gender, MultipartFile imageFile, LocalDate birth, MemberStatus status) {
		this.name = name;
		this.status = status;
		this.role = role;
		this.email = email;
		this.termsOfService = termsOfService;
		this.termsOfPrivacy = termsOfPrivacy;
		this.termsOfLocation = termsOfLocation;
		this.marketingConsent = marketingContent;
		this.gender = gender;
		this.imageFile = imageFile;
		this.birth = birth;
	}
}
