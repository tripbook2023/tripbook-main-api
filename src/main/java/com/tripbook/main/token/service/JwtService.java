package com.tripbook.main.token.service;

import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.member.entity.Member;

public interface JwtService {
	@Transactional
	public String saveToken(Member member);

}
