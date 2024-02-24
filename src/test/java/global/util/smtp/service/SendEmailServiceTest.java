package global.util.smtp.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tripbook.main.TripbookMainApiApplication;
import com.tripbook.main.global.util.smtp.service.SendEmailService;

@SpringBootTest(classes = TripbookMainApiApplication.class)
class SendEmailServiceTest {
	@Autowired
	SendEmailService sendEmailService;

	@Test
	@DisplayName("AWS_SES 발송테스트")
	void send() {
		List list = new ArrayList<String>();
		list.add("leeseokwoon@naver.com");
		sendEmailService.send("제목테스트", "Content!!", list);

	}

}