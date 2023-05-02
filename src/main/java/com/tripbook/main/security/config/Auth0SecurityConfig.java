package com.tripbook.main.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tripbook.main.auth.filter.OAuth2AccessTokenAuthenticationFilter;
import com.tripbook.main.member.controller.LogoutHandler;
import com.tripbook.main.security.filter.ExceptionHandlerFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Auth0SecurityConfig {
	@Autowired
	private LogoutHandler logoutHandler;
	@Autowired
	private final OAuth2AccessTokenAuthenticationFilter oAuth2AccessTokenAuthenticationFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests()
			.requestMatchers("/**")
			.permitAll();
		http
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.addLogoutHandler(logoutHandler);
		http
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.csrf().disable()
			.httpBasic().disable();
		http.addFilterBefore(
			new ExceptionHandlerFilter(),
			UsernamePasswordAuthenticationFilter.class
		);
		http.addFilterBefore(oAuth2AccessTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();

	}
}
