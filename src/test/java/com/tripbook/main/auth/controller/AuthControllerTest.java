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
class AuthControllerTest {

	@LocalServerPort
	int port;
	private static final String ACCESS_TOKEN = "eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiaXNzIjoiaHR0cHM6Ly9kZXYtejJiNGJhemZvNm81MzZ0ai51cy5hdXRoMC5jb20vIn0..zxmFYZl5j1FGA40l.pP20AF6LvGcJkm5iK3NEJocAFc-nxj3nZpKNfFKynuqQS5d9nwkuZcLqL6GQz439UppdZpKrY6ZJsUgO8Pl7DlUkrpQdAVHvkYPmhMaoQe-HRAUuVTF9lGJgHAq_QQd92m6qhunaKnpUOTdjoHPQO1R5DD5rkTwqOdFHnPGeXGDJTI6s0CZNA7M5Jc6O1rOSZGXTmvdBn1jDK7fS3OAcWrdC2vsXFtU5k9-4DKAtEDe-p1ZH7UTd4OogtRURZKGMfk4xQ_5kdZdOLi3iyRZrn53gJyPXtLT0Ml0Ydld0nrWjmAdcnXi-tMRWWblL3FHWGxcsafJo-jnaiJrOi0bZ8CWiaMAWXjo.eWd1exf4eFw3iE74TOw4Gw";

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