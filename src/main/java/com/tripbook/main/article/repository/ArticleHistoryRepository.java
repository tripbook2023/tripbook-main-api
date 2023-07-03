package com.tripbook.main.article.repository;

import com.tripbook.main.article.entity.ArticleHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleHistoryRepository extends JpaRepository<ArticleHistory, Long> {
}
