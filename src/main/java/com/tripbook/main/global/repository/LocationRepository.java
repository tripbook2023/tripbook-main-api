package com.tripbook.main.global.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbook.main.article.entity.Article;
import com.tripbook.main.global.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
	long deleteByArticle(Article article);
}
