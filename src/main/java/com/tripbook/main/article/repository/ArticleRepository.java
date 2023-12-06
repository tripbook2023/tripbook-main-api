package com.tripbook.main.article.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.article.entity.Article;
import com.tripbook.main.article.enums.ArticleStatus;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Slice<Article> findAllByStatus(ArticleStatus status, Pageable pageable);

	List<Article> findAllByStatusAndMemberEmail(ArticleStatus status, String email);

	Page<Article> findAllByStatusAndMemberEmail(ArticleStatus status, String email,Pageable pageable);

	Long deleteArticleById(Long id);

	Slice<Article> findAllByTitleContainingOrContentContainingAndStatus(String title, String content,
		ArticleStatus status, Pageable pageable);
}
