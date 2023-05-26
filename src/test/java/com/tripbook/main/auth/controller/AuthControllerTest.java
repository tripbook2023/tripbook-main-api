package com.tripbook.main.auth.controller;

import static com.tripbook.main.auth.controller.AuthControllerAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.auth.dto.AuthResponse;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class AuthControllerTest {

	@Autowired
	private AuthController sut;

	void login_with_auth0_accessToken() throws Exception {
		//given
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);

		//when
		final ResponseEntity<AuthResponse> response = sut.login(request);

		//then
		final AuthResponse responseBody = response.getBody();
		assertThat(responseBody.nickname()).isEqualTo("jay park");
		assertThat(responseBody.email()).isEqualTo("pjhyun7821@gmail.com");
		assertThat(responseBody.status()).isEqualTo("ADDITIONAL_AUTHENTICATION");
		assertThat(responseBody.refreshToken()).isNull();
		assertThat(responseBody.accessToken()).isNotNull();
	}

}