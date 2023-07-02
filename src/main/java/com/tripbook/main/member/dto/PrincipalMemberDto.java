package com.tripbook.main.member.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PrincipalMemberDto {
	private String email;
	private SimpleGrantedAuthority role;
}
