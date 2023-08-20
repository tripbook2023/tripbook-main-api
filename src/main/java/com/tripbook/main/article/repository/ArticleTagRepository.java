package com.tripbook.main.article.repository;

import com.tripbook.main.article.entity.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
}
