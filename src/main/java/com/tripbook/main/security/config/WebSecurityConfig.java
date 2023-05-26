package com.tripbook.main.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tripbook.main.member.controller.LogoutHandler;
import com.tripbook.main.member.repository.MemberRepository;
import com.tripbook.main.security.filter.ExceptionHandlerFilter;
import com.tripbook.main.security.filter.JwtTokenFilter;
import com.tripbook.main.security.handler.CustomLoginFailureHandler;
import com.tripbook.main.token.provider.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final LogoutHandler logoutHandler;
	private final JwtProvider jwtProvider;
	private final MemberRepository memberRepository;
	@Value("${spring.security.debug:false}")
	boolean securityDebug;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) ->
			web.debug(securityDebug)
				.ignoring()
				.requestMatchers(HttpMethod.GET, "/auth/login/oauth2/**");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//PlatForm 토큰 인증필터
		return http.csrf().disable()
			.headers().frameOptions().disable()
			.and()
			.authorizeHttpRequests()

			.anyRequest()
			.authenticated()

			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.formLogin().disable()
			.addFilterBefore(new JwtTokenFilter(memberRepository, jwtProvider),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new ExceptionHandlerFilter(), JwtTokenFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(new CustomLoginFailureHandler())
			.and()
			.build();
	}
}
