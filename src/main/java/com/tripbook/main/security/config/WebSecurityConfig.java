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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

import com.tripbook.main.auth.filter.OAuth2AccessTokenAuthenticationFilter;
import com.tripbook.main.member.controller.LogoutHandler;
import com.tripbook.main.security.filter.ExceptionHandlerFilter;
import com.tripbook.main.token.filter.JwtAuthenticationFilter;
import com.tripbook.main.token.provider.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final LogoutHandler logoutHandler;
	private final OAuth2AccessTokenAuthenticationFilter oAuth2AccessTokenAuthenticationFilter;
	private final JwtProvider jwtProvider;
	private final CorsConfigurationSource corsConfigurationSource;
	@Value("${spring.security.debug:false}")
	boolean securityDebug;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) ->
			web.debug(securityDebug);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();

		//PlatForm 토큰 인증필터
		http
			.authorizeHttpRequests()
			.requestMatchers(HttpMethod.GET, "/login/oauth2").authenticated()
			.and()
			.addFilterBefore(oAuth2AccessTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http
			.authorizeHttpRequests()
			.requestMatchers("/h2-console/**").permitAll()
			.requestMatchers(HttpMethod.POST, "/member/signup")
			.permitAll()
			.requestMatchers("/member/**")
			.hasRole("MEMBER")
			.anyRequest()
			.permitAll();

		http
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.addLogoutHandler(logoutHandler);
		http
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.csrf().disable()
			.httpBasic().disable()
			.cors().configurationSource(corsConfigurationSource);
		//JWT 인증 필터
		http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
		//Filter  ExceptionHandling
		http.addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class);

		return http.build();

	}
}
