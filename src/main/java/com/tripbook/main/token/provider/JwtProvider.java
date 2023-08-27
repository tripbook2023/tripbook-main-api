package com.tripbook.main.token.provider;

import java.security.Key;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.member.dto.PrincipalMemberDto;
import com.tripbook.main.member.entity.Member;
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

	/**
	 * 이메일 인증 토큰생성
	 * @param member
	 * @param deviceType
	 * @return
	 */
	public TokenInfo emailGenerateToken(Member member) {
		LocalDateTime now = LocalDateTime.now();
		// Access Token 생성
		LocalDateTime accessTokenExpiresIn = null;
		accessTokenExpiresIn = now.plusMinutes(10); // 10분
		String accessToken = Jwts.builder()
			.setSubject(member.getEmail())
			.claim("auth", member.getRole())
			.setExpiration(Date.from(accessTokenExpiresIn.atZone(ZoneId.systemDefault()).toInstant()))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
		return TokenInfo.builder().grantType("Bearer").accessToken(accessToken).build();
	}

	/** 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
	 * 토큰 유효시간
	 * WEB - 2시간
	 * MOBILE - 2시간
	 * APP - 1일 ,
	 */
	public TokenInfo generateToken(Member member, String deviceType) {
		LocalDateTime now = LocalDateTime.now();
		// Access Token 생성
		LocalDateTime accessTokenExpiresIn = null;
		LocalDateTime refreshTokenExpiresIn = null;
		//backdoor
		switch (deviceType) {
			case "WEB":
				accessTokenExpiresIn = now.plus(2, ChronoUnit.HOURS); // 2시간
				// accessTokenExpiresIn = now.plusMonths(3);
				// refreshTokenExpiresIn = new Date(now + 7200000);
				break;
			case "MOBILE":
				accessTokenExpiresIn = now.plus(2, ChronoUnit.HOURS); // 2시간
				// refreshTokenExpiresIn = new Date(now + (1 * 24 * 60 * 60 * 1000));
				break;
			case "APP":
				accessTokenExpiresIn = now.plusDays(1); // 1일
				refreshTokenExpiresIn = now.plusMonths(3);// 90일
				break;
			default:
				throw new CustomException.UnsupportedPlatform("Unsupported platform",
					ErrorCode.JWT_UNSUPPORTED_PLATFORM_ERROR);
		}
		String accessToken = Jwts.builder()
			.setSubject(member.getEmail())
			.claim("auth", member.getRole())
			.setExpiration(Date.from(accessTokenExpiresIn.atZone(ZoneId.systemDefault()).toInstant()))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
		// Refresh Token 생성
		if (deviceType.equals("APP")) {
			String refreshToken = Jwts.builder()
				.setSubject(member.getEmail())
				.setExpiration(Date.from(refreshTokenExpiresIn.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
			return TokenInfo.builder().grantType("Bearer").accessToken(accessToken).refreshToken(refreshToken).build();
		}

		return TokenInfo.builder().grantType("Bearer").accessToken(accessToken).build();
	}

	public String validateRefreshToken(String refreshToken) {
		return validateToken(refreshToken).getSubject();
	}

	// JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
	public PrincipalMemberDto getAuthentication(String token) {
		// 토큰 복호화
		Claims claims = validateToken(token);

		if (claims.get("auth") == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}

		// 클레임에서 권한 정보 가져오기
		return PrincipalMemberDto.builder()
			.role(new SimpleGrantedAuthority(claims.get("auth").toString()))
			.email(claims.getSubject())
			.build();
	}

	// JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
	public String getRefreshTokenAuthentication(String token) {
		// 토큰 복호화
		Claims claims = validateToken(token);
		// 클레임에서 권한 정보 가져오기
		return claims.getSubject();
	}

	// 토큰 정보를 검증하는 메서드
	public Claims validateToken(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.error("Invalid JWT Token", e);
			throw new CustomException.SecurityException(ErrorCode.JWT_INVALID_ERROR.getMessage(),
				ErrorCode.TOKEN_UNAUTHORIZED);
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT Token", e);
			throw new CustomException.ExpiredJwtException(ErrorCode.JWT_EXPIRED_ERROR.getMessage(),
				ErrorCode.JWT_EXPIRED_ERROR);
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT Token", e);
			throw new CustomException.UnsupportedJwtException(ErrorCode.JWT_UNSUPPORTED_ERROR.getMessage(),
				ErrorCode.JWT_UNSUPPORTED_ERROR);
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty.", e);
			throw new CustomException.IllegalArgumentException(ErrorCode.JWT_EMPTY_ERROR.getMessage(),
				ErrorCode.JWT_EMPTY_ERROR);
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