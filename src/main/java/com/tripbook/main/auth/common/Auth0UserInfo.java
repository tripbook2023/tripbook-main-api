package com.tripbook.main.auth.common;

public record Auth0UserInfo(
	String sub,
	String given_name,
	String family_name,
	String nickname,
	String name,
	String picture,
	String locale,
	String update_at,
	String email,
	String email_verified
) {
}
