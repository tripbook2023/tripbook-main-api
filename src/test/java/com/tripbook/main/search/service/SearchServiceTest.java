package com.tripbook.main.search.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tripbook.main.TripbookMainApiApplication;
import com.tripbook.main.article.service.ArticleService;
import com.tripbook.main.member.enums.Gender;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.enums.MemberStatus;
import com.tripbook.main.member.service.MemberService;
import com.tripbook.main.member.vo.MemberVO;

@SpringBootTest(classes = TripbookMainApiApplication.class)
class SearchServiceTest {
	@Autowired
	private SearchService searchService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ArticleService articleService;

	@BeforeEach
	public void createMember() {
		MemberVO memberVO = MemberVO.builder()
			.role(MemberRole.ROLE_EDITOR)
			.email("lso5507@nate.com")
			.name("이석운")
			.status(MemberStatus.STATUS_NORMAL)
			.gender(Gender.MALE)
			.termsOfService(true)
			.termsOfLocation(true)
			.termsOfPrivacy(true)
			.build();
		memberService.memberSave(memberVO, "WEB");

	}

	@Test
	void searchMember() {
		searchService.searchMember("이석운");
		searchService.searchMember("이석운");
	}
}