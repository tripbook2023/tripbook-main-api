package com.tripbook.main.article.repository;

import com.tripbook.main.article.entity.Article;
import com.tripbook.main.article.entity.ArticleBookmark;
import com.tripbook.main.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleBookmarkRepository extends JpaRepository<ArticleBookmark, Long> {
    Optional<ArticleBookmark> findByMemberAndArticle(Member member, Article article);
}
