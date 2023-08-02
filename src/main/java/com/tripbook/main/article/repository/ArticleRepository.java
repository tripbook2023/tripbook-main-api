package com.tripbook.main.article.repository;

import com.tripbook.main.article.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Slice<Article> findAllByTitleContainsAndContentContains(String title, String content, Pageable pageable);
}
