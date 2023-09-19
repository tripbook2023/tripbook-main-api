package com.tripbook.main.global.util.smtp.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.tripbook.main.global.util.smtp.dto.EmailSenderDto;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.token.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailService {

	private final AmazonSimpleEmailService amazonSimpleEmailService;
	@Qualifier("mailJwtService")
	private final JwtService mailJwtService;
	@Value("${cloud.aws.ses.from}")
	private String fromEmail;
	@Value("${spring.profiles.active}")
	private String activeProfile;
	@Value("${server.port}")
	private String serverPort;
	@Value("${domain.name}")
	private String domainName;
	public void send(final String subject, final String content, final List<Member> receivers) {
		final EmailSenderDto senderDto = EmailSenderDto.builder()
			.to(receivers.stream().map(Member::getEmail).toList())
			.subject(subject)
			.content(content)
			.fromEmail(fromEmail)
			.build();

		final SendEmailResult sendEmailResult = amazonSimpleEmailService
			.sendEmail(senderDto.toSendRequestDto());

		sendingResultMustSuccess(sendEmailResult);
	}

	private void sendingResultMustSuccess(final SendEmailResult sendEmailResult) {
		if (sendEmailResult.getSdkHttpMetadata().getHttpStatusCode() != 200) {
			log.error("{}", sendEmailResult.getSdkResponseMetadata().toString());
		}
	}

	public void tokenEmailSend(final String subject, final List<Member> receivers) {
		receivers.forEach(receiver -> {
			//TODO 사용자별 Content
			final EmailSenderDto senderDto = EmailSenderDto.builder()
				.to(Arrays.asList(receiver.getEmail()))
				.subject(subject)
				.content(this.createContent(mailJwtService.saveToken(receiver, null).getAccessToken()))
				.fromEmail(fromEmail)
				.build();
			final SendEmailResult sendEmailResult = amazonSimpleEmailService
				.sendEmail(senderDto.toSendRequestDto());
			sendingResultMustSuccess(sendEmailResult);
		});

	}

	private String createContent(String token) {
		StringBuilder sb = new StringBuilder();
		String targetURL = "";
		sb.append("<h2>[트립북] 이메일 인증 안내</h2><br>");
		sb.append("안녕하세요, 트립북입니다.<br>"
			+ "트립북을 이용해주셔서 진심으로 감사합니다.<br>"
			+ "하단의 버튼을 통해 이메일을 인증해주시기 바랍니다.<br>");

		targetURL = domainName+"/member/smtp?accessToken=" + token;
		sb.append("<a href=\"" + targetURL + "\">이메일인증하기</a><br>");
		sb.append("해당 이메일은 발송된 시점으로부터 10분간만 유효합니다.<br>"
			+ "감사합니다.<br><br>"
			+ "-------------------------------<br><br>"
			+ "해당 메일은 발신전용 메일입니다. 궁금하신 사항은 고객센터를 통해 문의해주시기 바랍니다.<br><br>"
			+ "ⓒTripbook Service All rights reserved");
		return sb.toString();

	}

}