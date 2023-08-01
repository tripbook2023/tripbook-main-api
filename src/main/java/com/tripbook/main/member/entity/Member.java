package com.tripbook.main.member.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.tripbook.main.member.dto.ResponseMember;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.tripbook.main.global.common.BasicEntity;
import com.tripbook.main.member.enums.Gender;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;
import com.tripbook.main.member.vo.MemberVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_MEMBER")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BasicEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false, unique = true)
	private String name;

	@Column
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberRole role;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birth;
	@Column
	private String profile;

	@Column(nullable = false)
	private Boolean termsOfService;

	@Column(nullable = false)
	private Boolean termsOfPrivacy;

	@Column(nullable = false)
	private Boolean termsOfLocation;

	@Column
	private Boolean marketingConsent;

	@Column
	private Long point;
	@Column
	private String appToken;
	@Column(nullable = false)
	private MemberStatus status;

	public void updateName(String name) {
		this.name = name;
	}

	public void updateRole(MemberRole role) {
		this.role = role;
	}

	public void updateProfile(String profile) {
		this.profile = profile;
	}

	public void updateTermsOfService(Boolean termsOfService) {
		this.termsOfService = termsOfService;
	}

	public void updateTermsOfPrivacy(Boolean termsOfPrivacy) {
		this.termsOfPrivacy = termsOfPrivacy;
	}

	public void updateTermsOfLocation(Boolean termsOfLocation) {
		this.termsOfLocation = termsOfLocation;
	}

	public void updateMarketingConsent(Boolean marketingConsent) {
		this.marketingConsent = marketingConsent;
	}

	public void updateAppToken(String appToken) {
		this.appToken = appToken;
	}

	public void updateStatus(MemberStatus status) {
		this.status = status;
	}

	public void updatePoint(Long point) {
		this.point = point;
	}

	public void updateBirth(LocalDate birth) {
		this.birth = birth;
	}

	public boolean isNotEditor() {
		return !(role.equals(MemberRole.ROLE_ADMIN) || role.equals(MemberRole.ROLE_EDITOR) || role.equals(MemberRole.ROLE_NAMED_EDITOR));
	}

	@Builder
	public Member(String email, String name, Gender gender, MemberRole role, MemberStatus status,
		Boolean termsOfService, Boolean termsOfPrivacy, Boolean termsOfLocation, Boolean marketingConsent,
		String profile) {
		this.email = email;
		this.profile = profile;
		this.name = name;
		this.gender = gender;
		this.role = role;
		this.status = status;
		this.termsOfService = termsOfService;
		this.termsOfPrivacy = termsOfPrivacy;
		this.termsOfLocation = termsOfLocation;
		this.marketingConsent = marketingConsent;
	}

	public Member(MemberVO memberVO) {
		this.status = memberVO.getStatus();
		this.email = memberVO.getEmail();
		this.role = memberVO.getRole();
		this.profile = memberVO.getProfile();
		this.birth = memberVO.getBirth();
		this.name = memberVO.getName();
		this.termsOfService = memberVO.getTermsOfService();
		this.termsOfPrivacy = memberVO.getTermsOfPrivacy();
		this.termsOfLocation = memberVO.getTermsOfLocation();
		this.marketingConsent = memberVO.getMarketingConsent();
	}

	public ResponseMember.MemberSimpleDto toSimpleDto() {
		return ResponseMember.MemberSimpleDto.builder()
				.id(this.id)
				.name(this.name)
				.profileUrl(this.profile)
				.role(role)
				.build();
	}
}