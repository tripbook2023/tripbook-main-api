import com.tripbook.main.article.dto.ArticleRequestDto;
import com.tripbook.main.article.enums.ArticleStatus;
import com.tripbook.main.article.service.ArticleService;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;
import com.tripbook.main.member.service.MemberService;
import com.tripbook.main.member.vo.MemberVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tripbook.main.TripbookMainApiApplication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;

@SpringBootTest(classes = TripbookMainApiApplication.class)
public class TestApplication {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private MemberService memberService;


	@Test
	public void contextLoad() {

	}

	@Test
	public void saveArticle() {
		MemberVO member = MemberVO.builder()
				.email("test@test.com")
				.name("test")
				.termsOfLocation(true)
				.termsOfPrivacy(true)
				.termsOfService(true)
				.status(MemberStatus.STATUS_NORMAL)
				.role(MemberRole.ROLE_EDITOR)
				.build();

		memberService.memberSave(member, "WEB");

		ArticleRequestDto.ArticleSaveRequest request = ArticleRequestDto.ArticleSaveRequest.builder()
																		.title("test")
																		.content("test")
																		.build();

		OAuth2User principal = new DefaultOAuth2User(null, Collections.singletonMap("email", "test@test.com"),
				"email");

		System.out.println(articleService.saveArticle(request, ArticleStatus.PRE_JUDGEMENT, principal));

	}
}
