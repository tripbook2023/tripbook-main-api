package com.tripbook.main.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.util.CustomTokenUtil;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.repository.MemberRepository;
import com.tripbook.main.token.provider.JwtProvider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = CustomTokenUtil.resolveToken(request);

		final Claims claims = jwtProvider.validateToken(token);

		Member member = memberRepository.findOptionalByEmail(claims.getSubject())
			.orElseThrow(() -> {
				log.error("User does not exist");
				return new CustomException.MemberNotFound(ErrorCode.MEMBER_NOTFOUND.getMessage(),
					ErrorCode.MEMBER_NOTFOUND);
			});

		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(
				member,
				null,
				List.of(new SimpleGrantedAuthority(member.getRole().getValue()))
			);

		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		filterChain.doFilter(request, response);
	}
}
