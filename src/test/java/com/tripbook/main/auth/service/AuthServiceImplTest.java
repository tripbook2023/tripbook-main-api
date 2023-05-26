package com.tripbook.main.auth.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthServiceImplTest {

	@Autowired
	private AuthServiceImpl sut;

	@Value("${auth.access-token}")
	public String accessToken;

	@PersistenceContext
	EntityManager em;

	@Test
	void login_with_auth0_accessToken() throws Exception {

		//when
		final LoginUserInfo info = sut.login(accessToken);

		//then
		final Optional<Member> member = em.createQuery("select m from Member m where m.email = :email", Member.class)
			.setParameter("email", info.email())
			.getResultList().stream().findAny();

		assertThat(member).isPresent();
		assertThat(info.nickname()).isEqualTo("jay park");
		assertThat(info.email()).isEqualTo("pjhyun7821@gmail.com");
		assertThat(info.status()).isEqualTo(MemberStatus.ADDITIONAL_AUTHENTICATION);
		assertThat(info.refreshToken()).isNull();
		assertThat(info.accessToken()).isNotNull();

	}
}