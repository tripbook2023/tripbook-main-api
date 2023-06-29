package com.tripbook.main.article.repository;

import com.tripbook.main.article.entity.ArticleBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleBookmarkRepository extends JpaRepository<ArticleBookmark, Long> {
}
