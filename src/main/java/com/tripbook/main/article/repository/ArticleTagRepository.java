package com.tripbook.main.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.article.entity.Article;
import com.tripbook.main.article.entity.ArticleTag;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
	void deleteAllByArticle(Article article);
}
