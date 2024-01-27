package com.tripbook.main.article.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.article.entity.Article;
import com.tripbook.main.article.entity.ArticleReport;
import com.tripbook.main.member.entity.Member;

public interface ArticleReportRepository extends JpaRepository<ArticleReport, Long> {
	Optional<ArticleReport> findByAndArticleAndMember(Article article, Member member);
}
