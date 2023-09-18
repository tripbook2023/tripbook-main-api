package com.tripbook.main.article.service;

import com.tripbook.main.article.dto.ArticleRequestDto;
import com.tripbook.main.article.dto.ArticleResponseDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface ArticleTempService {
    ArticleResponseDto.ArticleResponse saveArticleTemp(ArticleRequestDto.ArticleSaveRequest requestDto, OAuth2User principal);
}
