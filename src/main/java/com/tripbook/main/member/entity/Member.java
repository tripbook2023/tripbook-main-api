package com.tripbook.main.member.entity;

import java.io.Serializable;
import java.util.Date;

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

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberRole role;
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birth;
	@Column
	private String profile;
	@Column(nullable = false)
	private Boolean isMarketing;
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

	public void updateIsMarketing(Boolean isMarketing) {
		this.isMarketing = isMarketing;
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

	public void updateBirth(Date birth) {
		this.birth = birth;
	}

	@Builder
	public Member(String email, String name, Gender gender, MemberRole role, MemberStatus status, Boolean isMarketing,
		String profile) {
		this.email = email;
		this.profile = profile;
		this.name = name;
		this.gender = gender;
		this.role = role;
		this.status = status;
		this.isMarketing = isMarketing;
	}

	public Member(MemberVO memberVO) {
		this.status = memberVO.getStatus();
		this.email = memberVO.getEmail();
		this.role = memberVO.getRole();
		this.profile = memberVO.getProfile();
		this.birth = memberVO.getBirth();
		this.name = memberVO.getName();
		this.isMarketing = memberVO.isMarketing();
	}
}