package com.tripbook.main.member.vo;

import java.sql.Date;

import com.tripbook.main.member.enums.Gender;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class MemberVO {
	private String name;
	private String email;
	private boolean isMarketing;
	private Gender gender;
	private String profile;
	private Date birth;

	@Builder
	public MemberVO(String name, String email, boolean isMarketing, Gender gender, String profile, Date birth) {
		this.name = name;
		this.email = email;
		this.isMarketing = isMarketing;
		this.gender = gender;
		this.profile = profile;
		this.birth = birth;
	}
}
