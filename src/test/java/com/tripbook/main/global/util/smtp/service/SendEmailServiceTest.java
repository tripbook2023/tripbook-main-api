package com.tripbook.main.global.util.smtp.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tripbook.main.TripbookMainApiApplication;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberRole;

@SpringBootTest(classes = TripbookMainApiApplication.class)
class SendEmailServiceTest {
	@Autowired
	SendEmailService sendEmailService;
	private List list = new ArrayList<Member>();

	@BeforeEach
	void setEmailList() {
		this.list.add(Member.builder().role(MemberRole.ROLE_MEMBER).email("leeseokwoon@naver.com").build());
	}

	@Test
	@DisplayName("AWS_SES 발송테스트")
	void send() {
		sendEmailService.send("제목테스트", "Content!!", this.list);
	}

	@Test
	@DisplayName("토큰_이메일인증구현")
	void tokenEmailSend() {
		sendEmailService.tokenEmailSend("트립북_이메일인증", this.list);
	}
}