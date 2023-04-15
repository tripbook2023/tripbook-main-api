package com.tripbook.main.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tripbook.main.member.controller.LogoutHandler;

@Configuration
@EnableWebSecurity
public class Auth0SecurityCoifng {
	@Autowired
	private LogoutHandler logoutHandler;


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.oauth2Login()
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.addLogoutHandler(logoutHandler)
			.and()
			.build();
	}
}
