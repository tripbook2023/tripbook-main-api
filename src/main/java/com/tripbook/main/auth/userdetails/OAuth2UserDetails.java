package com.tripbook.main.auth.userdetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Author : 이석운
 * @TODO : 임시용 UserDetails -> 추후 필요 데이터 추가
 */
@AllArgsConstructor
@Builder
public class OAuth2UserDetails implements UserDetails {

	// private SocialType socialType;
	private String username;
	private String email;
	private Set<GrantedAuthority> authorities;

	public String getEmail(){
		return this.email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}


	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}