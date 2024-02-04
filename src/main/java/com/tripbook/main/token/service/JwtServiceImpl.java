package com.tripbook.main.token.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.token.dto.TokenInfo;
import com.tripbook.main.token.entity.JwtToken;
import com.tripbook.main.token.enums.DeviceValue;
import com.tripbook.main.token.provider.JwtProvider;
import com.tripbook.main.token.repository.JwtRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("jwtService")
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
	private final JwtRepository jwtRepository;
	private final JwtProvider jwtProvider;

	/**
	 * 토큰 유효성검사
	 * @param token - 토큰정보
	 * @param device - 접근 디바이스 정보
	 * @return TokenInfo
	 */
	@Override
	public TokenInfo tokenIssue(String token, String device) {
		//토큰에서 member추출
		String findEmail = jwtProvider.getRefreshTokenAuthentication(token);
		// RefreshToken 소유자 확인 && 최초 로그인 시 Device 종류 동일한지 체크
		List<Member> members = jwtRepository.findByEmail(findEmail, DeviceValue.valueOf(device));
		if (!members.isEmpty()) {
			// 리프레시토큰과 액세스토큰 재발급
			TokenInfo tokenInfo = jwtProvider.generateToken(members.get(0), device);
			return tokenInfo;
		} else {
			throw new CustomException.NotFoundJwtException("NotFound_RefreshToken", ErrorCode.JWT_NOTFOUND_ERROR);
		}
	}

	/**
	 * 토큰 DB 저장
	 * @param member
	 * @param deviceType
	 * @return
	 */
	@Transactional
	@Override
	public TokenInfo saveToken(Member member, String deviceType) {
		TokenInfo tokenInfo = jwtGenerateToken(member, deviceType);
		if ("APP".equals(deviceType)) {
			List<JwtToken> tokens = jwtRepository.findByDeviceAndMemberId(DeviceValue.valueOf(deviceType),
				member);
			JwtToken jwtToken = null;
			//처음
			if (tokens.size() < 1) {
				jwtToken = JwtToken.builder()
					.device(DeviceValue.valueOf(deviceType))
					.token(tokenInfo.getRefreshToken())
					.memberId(member)
					.build();
			} else {
				jwtToken = tokens.get(0);
				jwtToken.updateToken(tokenInfo.getRefreshToken());
			}
			jwtRepository.save(jwtToken);
		}
		return tokenInfo;
	}

	private void duplicateTokenRemove(JwtToken jwtToken) {
		jwtRepository.deleteByDeviceAndMemberId(jwtToken.getDevice(), jwtToken.getMemberId());
	}

	/**
	 * 토큰 발급
	 * @param member
	 * @param deviceType
	 * @return
	 */
	private TokenInfo jwtGenerateToken(Member member, String deviceType) {
		return jwtProvider.generateToken(member, deviceType);
	}

}
