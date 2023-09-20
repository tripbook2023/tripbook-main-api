package com.tripbook.main.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tripbook.main.article.entity.Article;
import com.tripbook.main.article.service.ArticleService;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.service.MemberService;
import com.tripbook.main.search.entity.Search;
import com.tripbook.main.search.repository.SearchRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
	private final MemberService memberService;
	private final ArticleService articleService;
	private final SearchRepository searchRepository;

	@Transactional
	public List<Member> searchMember(String keyword) {
		log.info("Searching MemberKeyword:::{}", keyword);
		List<Member> members = memberService.selectMemberList(keyword);
		log.info("Searched MemberSize:::{}", members.size());
		//Search Logging
		upsertSearchLogging(keyword);
		return members;
	}

	private void upsertSearchLogging(String keyword) {
		Search resultContent = searchRepository.findByContent(keyword);
		if (resultContent != null)
			resultContent.updateCount();
		else
			searchRepository.save(new Search(keyword));

	}

	public List<Article> searchArticle() {
		return null;
	}
	//연관검색어 조회

	//인기검색어 조회

}
