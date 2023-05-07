package com.tripbook.main.token.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.member.entity.Member;
import com.tripbook.main.token.dto.TokenInfo;
import com.tripbook.main.token.entity.JwtToken;
import com.tripbook.main.token.enums.DeviceValue;
import com.tripbook.main.token.provider.JwtProvider;
import com.tripbook.main.token.repository.JwtRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
	private final JwtRepository jwtRepository;

	private final JwtProvider jwtProvider;

	@Transactional
	@Override
	public String saveToken(Member member, String deviceType) {
		TokenInfo tokenInfo = jwtGenerateToken(member, deviceType);
		JwtToken jwtToken = JwtToken.builder()
			.device(DeviceValue.valueOf(deviceType))
			.token(tokenInfo.getRefreshToken())
			.memberId(member)
			.build();
		duplicateTokenRemove(jwtToken);
		jwtRepository.save(jwtToken);
		return tokenInfo.getAccessToken();
	}

	private void duplicateTokenRemove(JwtToken jwtToken) {
		jwtRepository.deleteByDeviceAndMemberId(jwtToken.getDevice(), jwtToken.getMemberId());
	}

	private TokenInfo jwtGenerateToken(Member member, String deviceType) {
		return jwtProvider.generateToken(member, deviceType);
	}

}
