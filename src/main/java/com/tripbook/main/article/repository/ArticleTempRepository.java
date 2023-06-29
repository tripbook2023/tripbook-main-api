package com.tripbook.main.article.repository;

import com.tripbook.main.article.entity.ArticleTemp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTempRepository extends JpaRepository<ArticleTemp, Long> {
}
