package com.tripbook.main.auth.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerAcceptanceTest {

	@LocalServerPort
	int port;
	public static final String ACCESS_TOKEN = "";

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	void login_with_auth0_accessToken() throws Exception {
		//when
		final JsonPath response = RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + ACCESS_TOKEN).

			when()
			.get("/login/oauth2/").

			then()
			.log().all()
			.extract()
			.body().jsonPath();

		//then
		assertThat(response.getString("nickname")).isEqualTo("jay park");
		assertThat(response.getString("email")).isEqualTo("pjhyun7821@gmail.com");
		assertThat(response.getString("status")).isEqualTo("ADDITIONAL_AUTHENTICATION");
		assertThat(response.getString("refreshToken")).isNull();
		assertThat(response.getString("accessToken")).isNotNull();
	}
}