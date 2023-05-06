package com.tripbook.main.token.provider;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.member.dto.PrincipalMemberDto;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.token.dto.TokenInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

	private final Key key;

	public JwtProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	// 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
	public TokenInfo generateToken(Member member) {
		long now = (new Date()).getTime();
		// Access Token 생성
		Date accessTokenExpiresIn = new Date(now + 86400000);
		String accessToken = Jwts.builder()
			.setSubject(member.getEmail())
			.claim("auth", member.getRole())
			.setExpiration(accessTokenExpiresIn)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
		// Refresh Token 생성
		String refreshToken = Jwts.builder()
			.setExpiration(new Date(now + 86400000))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

		return TokenInfo.builder().grantType("Bearer").accessToken(accessToken).refreshToken(refreshToken).build();
	}

	// JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
	public PrincipalMemberDto getAuthentication(String accessToken) {
		// 토큰 복호화
		Claims claims = parseClaims(accessToken);

		if (claims.get("auth") == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}

		// 클레임에서 권한 정보 가져오기
		return PrincipalMemberDto.builder()
			.role(MemberRole.valueOf((String)claims.get("auth")))
			.email(claims.getSubject())
			.build();
	}

	// 토큰 정보를 검증하는 메서드
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.error("Invalid JWT Token", e);
			throw new CustomException.SecurityException(ErrorCode.JWT_INVALID_ERROR.getMessage(),
				ErrorCode.TOKEN_UNAUTHORIZED);
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT Token", e);
			throw new CustomException.ExpiredJwtException(ErrorCode.JWT_EXPIRED_ERROR.getMessage(),
				ErrorCode.TOKEN_UNAUTHORIZED);
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT Token", e);
			throw new CustomException.UnsupportedJwtException(ErrorCode.JWT_UNSUPPORTED_ERROR.getMessage(),
				ErrorCode.TOKEN_UNAUTHORIZED);
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty.", e);
			throw new CustomException.IllegalArgumentException(ErrorCode.JWT_EMPTY_ERROR.getMessage(),
				ErrorCode.TOKEN_UNAUTHORIZED);
		}
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}