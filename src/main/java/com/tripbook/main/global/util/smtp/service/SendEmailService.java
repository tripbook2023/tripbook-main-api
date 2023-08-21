package com.tripbook.main.global.util.smtp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.tripbook.main.global.util.smtp.dto.EmailSenderDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailService {

	private final AmazonSimpleEmailService amazonSimpleEmailService;
	@Value("${cloud.aws.ses.from}")
	private String fromEmail;

	public void send(final String subject, final String content, final List<String> receivers) {
		final EmailSenderDto senderDto = EmailSenderDto.builder()
			.to(receivers)
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

}