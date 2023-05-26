package com.tripbook.main.auth.common;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserInfoRequestV2Test {

	@Autowired
	UserInfoRequestV2 sut;

	@Value("${auth.access-token}")
	public String accessToken;

	@Test
	void auth0_accessToken_test() throws Exception {
		//when
		final Auth0UserInfo response = sut.getUserInfoFromAuth0Token(accessToken);
		//then
		assertThat(response.email()).isEqualTo("pjhyun7821@gmail.com");

	}
}