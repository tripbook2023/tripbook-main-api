package com.tripbook.main.member.dto;

import com.tripbook.main.member.enums.MemberRole;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PrincipalMemberDto {
	private String email;
	private MemberRole role;
}
