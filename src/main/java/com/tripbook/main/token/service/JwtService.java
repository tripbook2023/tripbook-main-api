package com.tripbook.main.token.service;

import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.member.entity.Member;
import com.tripbook.main.token.dto.TokenInfo;

public interface JwtService {
	@Transactional
	public TokenInfo saveToken(Member member, String deviceType);

}
