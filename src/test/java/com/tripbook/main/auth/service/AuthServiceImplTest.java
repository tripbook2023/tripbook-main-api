package com.tripbook.main.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.tripbook.main.member.entity.Member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthServiceImplTest {

	@Autowired
	private AuthServiceImpl sut;

	@PersistenceContext
	EntityManager em;

	@Test
	void login_with_auth0_accessToken() throws Exception {

		//when
		final LoginUserInfo info = sut.login(ACCESS_TOKEN);

		//then
		final Optional<Member> member = em.createQuery("select m from Member m where m.email = :email", Member.class)
			.setParameter("email", info.email())
			.getResultList().stream().findAny();

		assertThat(member).isPresent();
		assertThat(info.nickname()).isEqualTo("jay park");
		assertThat(info.email()).isEqualTo("pjhyun7821@gmail.com");
		assertThat(info.status()).isEqualTo("ADDITIONAL_AUTHENTICATION");
		assertThat(info.refreshToken()).isNull();
		assertThat(info.accessToken()).isNotNull();

	}
}