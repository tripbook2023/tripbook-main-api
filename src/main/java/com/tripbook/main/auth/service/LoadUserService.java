package com.tripbook.main.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tripbook.main.auth.common.UserInfoRequest;
import com.tripbook.main.auth.dto.ResponseAuth.ResultInfo;
import com.tripbook.main.auth.token.CustomPlatformAccessToken;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberStatus;
import com.tripbook.main.member.service.MemberService;
import com.tripbook.main.member.vo.MemberVO;
import com.tripbook.main.token.dto.TokenInfo;
import com.tripbook.main.token.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class LoadUserService {
	private final UserInfoRequest userInfoRequest;
	private final JwtService jwtService;
	private final MemberService memberService;

	@SneakyThrows
	public ResultInfo getOAuth2UserDetails(CustomPlatformAccessToken authentication) {
		MemberVO memberVO = userInfoRequest.getSocialEmail(authentication.getAccessToken());
		Optional<Member> resultMember = memberService.memberCertification(memberVO);

		if (!resultMember.isPresent()) {
			//회원가입 필요
			return getSignUpMessage(memberVO);
		} else {
			Member member = resultMember.get();
			TokenInfo tokenInfo = savaJwt(member, ((CustomPlatformAccessToken)authentication).getDevice());
			return getSingInMessage(member, tokenInfo);
		}
	}

	private static ResultInfo getSingInMessage(Member member, TokenInfo tokenInfo) {
		return ResultInfo.builder()
			.status(member.getStatus())
			.nickname(member.getName())
			.email(member.getEmail())
			.refreshToken(tokenInfo.getRefreshToken())
			.accessToken(tokenInfo.getAccessToken())
			.build();
	}

	private static ResultInfo getSignUpMessage(MemberVO memberVO) {
		return ResultInfo.builder()
			.email(memberVO.getEmail())
			.nickname(memberVO.getName())
			.status(MemberStatus.STATUS_REQUIRED_AUTH)
			.build();
	}

	private TokenInfo savaJwt(Member saveMember, String deviceType) {
		return jwtService.saveToken(saveMember, deviceType);
	}

}
