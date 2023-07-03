package com.tripbook.main.article.repository;

import com.tripbook.main.article.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
