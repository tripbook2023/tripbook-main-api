package com.tripbook.main.article.repository;

import com.tripbook.main.article.entity.Article;
import com.tripbook.main.article.entity.ArticleHeart;
import com.tripbook.main.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleHeartRepository extends JpaRepository<ArticleHeart, Long> {
    Optional<ArticleHeart> findByMemberAndArticle(Member member, Article article);
    Long countByArticle(Article article);
}
