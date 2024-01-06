package com.tripbook.main.article.repository;

import java.util.List;

import com.tripbook.main.article.entity.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
	void deleteAllByArticleId(Long articleId);
}
