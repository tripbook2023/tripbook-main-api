package com.tripbook.main.token.service;

import com.tripbook.main.member.entity.Member;
import com.tripbook.main.token.dto.TokenInfo;

public interface JwtService {

	TokenInfo saveToken(Member member, String deviceType);

	TokenInfo tokenIssue(String token, String device);
}
