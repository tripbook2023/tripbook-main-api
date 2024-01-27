package com.tripbook.main.article.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.tripbook.main.article.dto.ArticleRequestDto;
import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.enums.ArticleStatus;

public interface ArticleService {
	ArticleResponseDto.ArticleResponse saveArticle(ArticleRequestDto.ArticleSaveRequest requestDto,
		ArticleStatus status, OAuth2User principal);

	Slice<ArticleResponseDto.ArticleResponse> searchArticle(String word, Pageable pageable, OAuth2User principal);

	ArticleResponseDto.ArticleResponse getArticleDetail(long articleId, OAuth2User principal);

	void deleteArticle(long articleId, OAuth2User principal);

	ArticleResponseDto.ArticleResponse saveArticleComment(long articleId,
		ArticleRequestDto.CommentSaveRequest requestDto, OAuth2User principal);

	void deleteArticleComment(long commentId, OAuth2User principal);

	ArticleResponseDto.ArticleResponse likeArticle(long articleId, OAuth2User principal);

	ArticleResponseDto.ArticleResponse bookmarkArticle(long articleId, OAuth2User principal);

	ArticleResponseDto.ArticleResponse reportArticle(ArticleRequestDto.ReportRequest requestDto, OAuth2User principal);
}
