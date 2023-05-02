package com.tripbook.main.auth.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.tripbook.main.auth.service.LoadUserService;
import com.tripbook.main.auth.token.CustomAccessToken;
import com.tripbook.main.auth.userdetails.OAuth2UserDetails;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {
	private final LoadUserService loadUserService;
	private final MemberRepository memberRepository;

	@SneakyThrows
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		OAuth2UserDetails oAuth2User = loadUserService.getOAuth2UserDetails((CustomAccessToken)authentication);
		userValidation(oAuth2User);
		return CustomAccessToken.builder().principal(oAuth2User).authorities(oAuth2User.getAuthorities()).build();
	}

	private void userValidation(OAuth2UserDetails oAuth2User) throws Exception {
		Member member = memberRepository.findByEmail(oAuth2User.getEmail());
		try {
			if (member != null) {
				// 중복 회원가입 방지
				log.error("Member_Already_Exists.");
				throw new BadCredentialsException("Member_Already_Exists");
			}
		} catch (Exception e) {
			log.error("MemberSaveError::", e);
			throw new Exception("MemberSaveError", e);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return CustomAccessToken.class.isAssignableFrom(
			authentication);
	}

}
