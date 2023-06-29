package com.tripbook.main.article.repository;

import com.tripbook.main.article.entity.ArticleCommentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentHistoryRepository extends JpaRepository<ArticleCommentHistory, Long> {
}
