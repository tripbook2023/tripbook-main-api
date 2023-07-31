package com.tripbook.main.global.util.smtp.dto;

import java.util.List;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailSenderDto {

	private final List<String> to; // 받는 사람
	private final String subject; // 제목
	private final String content; // 본문
	private final String fromEmail; // 보내는사람

	@Builder
	public EmailSenderDto(final List<String> to, final String subject,
		final String content, final String fromEmail) {
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.fromEmail = fromEmail;
	}

	public SendEmailRequest toSendRequestDto() {
		final Destination destination = new Destination()
			.withToAddresses(this.to);

		final Message message = new Message()
			.withSubject(createContent(this.subject))
			.withBody(new Body()
				.withHtml(createContent(this.content)));

		return new SendEmailRequest()
			.withSource(fromEmail)
			.withDestination(destination)
			.withMessage(message);
	}

	private Content createContent(final String text) {
		return new Content()
			.withCharset("UTF-8")
			.withData(text);
	}
}