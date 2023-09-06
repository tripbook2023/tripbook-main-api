package com.tripbook.main.token.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.repository.MemberRepository;
import com.tripbook.main.token.dto.TokenInfo;
import com.tripbook.main.token.provider.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("mailJwtService")
@RequiredArgsConstructor
@Slf4j
public class MailJwtServiceImpl implements JwtService {
	private final JwtProvider jwtProvider;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public TokenInfo tokenIssue(String token, String device) {
		//TODO 이메일 인증 JWT{email,createdAt}
		//토큰에서 email추출
		String findEmail = jwtProvider.getRefreshTokenAuthentication(token);
		Member member = memberRepository.findByEmail(findEmail);
		if (member.getRole().equals(MemberRole.ROLE_NAMED_EDITOR)) {
			log.info("Verification email::" + findEmail);
			//TODO Response으로 보낼 예정
			log.info("이미 인증된 회원");
			return null;
		}
		member.updateRole(MemberRole.ROLE_NAMED_EDITOR);
		log.info("Verification email::" + findEmail);
		//@Todo 성공여부 판단후에 단순 이메일 인증 또는 에디터 신청에 따른 멤버 권한변경
		return null;
	}

	@Transactional
	@Override
	public TokenInfo saveToken(Member member, String deviceType) {
		return this.jwtGenerateToken(member, deviceType);
	}

	private TokenInfo jwtGenerateToken(Member member, String deviceType) {
		return jwtProvider.emailGenerateToken(member);
	}

}
