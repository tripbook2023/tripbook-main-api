package com.tripbook.main.article.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbook.main.article.dto.ArticleRequestDto;
import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.entity.Article;
import com.tripbook.main.article.entity.ArticleBookmark;
import com.tripbook.main.article.entity.ArticleComment;
import com.tripbook.main.article.entity.ArticleHeart;
import com.tripbook.main.article.entity.ArticleImage;
import com.tripbook.main.article.entity.ArticleTag;
import com.tripbook.main.article.enums.ArticleCommentStatus;
import com.tripbook.main.article.enums.ArticleStatus;
import com.tripbook.main.article.repository.ArticleBookmarkRepository;
import com.tripbook.main.article.repository.ArticleCommentRepository;
import com.tripbook.main.article.repository.ArticleHeartRepository;
import com.tripbook.main.article.repository.ArticleImageRepository;
import com.tripbook.main.article.repository.ArticleRepository;
import com.tripbook.main.article.repository.ArticleTagRepository;
import com.tripbook.main.file.service.UploadService;
import com.tripbook.main.global.entity.Image;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.repository.ImageRepository;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
	private final MemberService memberService;
	private final UploadService uploadService;
	private final ArticleRepository articleRepository;
	private final ImageRepository imageRepository;
	private final ArticleImageRepository articleImageRepository;
	private final ArticleTagRepository articleTagRepository;
	private final ArticleCommentRepository articleCommentRepository;
	private final ArticleBookmarkRepository articleBookmarkRepository;
	private final ArticleHeartRepository articleHeartRepository;

	@Override
	@Transactional
	public ArticleResponseDto.ArticleResponse saveArticle(ArticleRequestDto.ArticleSaveRequest requestDto,
		ArticleStatus status, OAuth2User principal) {
		Member loginMember = getLoginMemberByPrincipal(principal);
		Article article;
		if (requestDto.getArticleId() != null) {
			Optional<Article> resultDto = articleRepository.findById(requestDto.getArticleId());
			article = resultDto.orElseGet(() -> articleRepository.save(Article.builder()
				.title(requestDto.getTitle())
				.content(requestDto.getContent())
				.member(loginMember)
				.status(status)
				.build()));

		} else {
			article = articleRepository.save(Article.builder()
				.title(requestDto.getTitle())
				.content(requestDto.getContent())
				.member(loginMember)
				.status(status)
				.build());
		}
		if (loginMember == null) {
			throw new CustomException.MemberNotFound(ErrorCode.MEMBER_NOTFOUND.getMessage(), ErrorCode.MEMBER_NOTFOUND);
		}
/*
        if (loginMember.isNotEditor()) {
            throw new CustomException.MemberNotPermittedException(ErrorCode.MEMBER_NOT_PERMITTED.getMessage(), ErrorCode.MEMBER_NOT_PERMITTED);
        }

 */

		ArticleResponseDto.ArticleResponse response = article.toDto(loginMember);
		//이미지 리스트 저장
		if (requestDto.getImageList() != null) {
			List<ArticleImage> imageList = requestDto.getImageList().stream()
				.map(file -> {
					String resultUrl = uploadService.imageUpload(file, "article");
					Image image = imageRepository.save(
						Image.builder().url(resultUrl).name(file.getOriginalFilename()).build());
					return articleImageRepository.save(
						ArticleImage.builder().image(image).article(article).isThumbnail(false).build());

				}).toList();

			response.setImageList(imageList.stream().map(ArticleImage::toDto).toList());
		}
		//썸네일 이미지 저장
		if (requestDto.getThumbnail() != null) {
			String resultUrl = uploadService.imageUpload(requestDto.getThumbnail(), "article");
			Image image = imageRepository.save(
				Image.builder().url(resultUrl).name(requestDto.getThumbnail().getOriginalFilename()).build());
			ArticleImage articleImage = articleImageRepository.save(
				ArticleImage.builder().image(image).article(article).isThumbnail(true).build());
			response.setThumbnail(articleImage.toDto());
		}
		if (requestDto.getTagList() != null) {
			List<ArticleTag> tagList = requestDto.getTagList().stream()
				.map(tag -> articleTagRepository.save(ArticleTag.builder().name(tag).article(article).build()))
				.toList();
			response.setTagList(tagList.stream().map(ArticleTag::getName).toList());
		}

		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<ArticleResponseDto.ArticleResponse> searchArticle(String word, Pageable pageable,
		OAuth2User principal) {
		Member loginMember = getLoginMemberByPrincipal(principal);

		if (word == null || word.equals("")) {
			return articleRepository
				.findAllByStatus(ArticleStatus.ACTIVE, pageable)
				.map(article -> article.toDto(loginMember));
		}

		return articleRepository
			.findAllByTitleContainingOrContentContainingAndStatus(word, word, ArticleStatus.ACTIVE, pageable)
			.map(article -> article.toDto(loginMember));
	}

	@Override
	@Transactional(readOnly = true)
	public ArticleResponseDto.ArticleResponse getArticleDetail(long articleId, OAuth2User principal) {
		Article article = articleRepository.findById(articleId).orElseThrow(
			() -> new CustomException.ArticleNotFoundException(ErrorCode.ARTICLE_NOT_FOUND.getMessage(),
				ErrorCode.ARTICLE_NOT_FOUND)
		);

		Member loginMember = getLoginMemberByPrincipal(principal);

		if (!article.getStatus().equals(ArticleStatus.DELETED)) {
			throw  new CustomException.ArticleNotFoundException(ErrorCode.ARTICLE_NOT_FOUND.getMessage(),
				ErrorCode.ARTICLE_NOT_FOUND);
		}

		return article.toDto(loginMember);
	}

	@Override
	@Transactional
	public void deleteArticle(long articleId, OAuth2User principal) {
		Article article = articleRepository.findById(articleId).orElseThrow(
			() -> new CustomException.ArticleNotFoundException(ErrorCode.ARTICLE_NOT_FOUND.getMessage(),
				ErrorCode.ARTICLE_NOT_FOUND)
		);

		Member loginMember = getLoginMemberByPrincipal(principal);

		if (loginMember == null) {
			throw new CustomException.MemberNotFound(ErrorCode.MEMBER_NOTFOUND.getMessage(), ErrorCode.MEMBER_NOTFOUND);
		}

		if (!article.isWrittenBy(loginMember) && !loginMember.isAdmin()) {
			throw new CustomException.MemberNotPermittedException(ErrorCode.MEMBER_NOT_PERMITTED.getMessage(),
				ErrorCode.MEMBER_NOT_PERMITTED);
		}

		article.delete();
	}

	@Override
	@Transactional
	public ArticleResponseDto.ArticleResponse saveArticleComment(long articleId,
		ArticleRequestDto.CommentSaveRequest requestDto, OAuth2User principal) {
		Article article = articleRepository.findById(articleId).orElseThrow(
			() -> new CustomException.ArticleNotFoundException(ErrorCode.ARTICLE_NOT_FOUND.getMessage(),
				ErrorCode.ARTICLE_NOT_FOUND)
		);

		Member loginMember = getLoginMemberByPrincipal(principal);

		if (loginMember == null) {
			throw new CustomException.MemberNotPermittedException(ErrorCode.MEMBER_NOT_PERMITTED.getMessage(),
				ErrorCode.MEMBER_NOT_PERMITTED);
		}

		ArticleComment parent = articleCommentRepository.findById(requestDto.getParentId()).orElse(null);

		ArticleComment comment = ArticleComment.builder()
			.member(loginMember)
			.article(article)
			.content(requestDto.getContent())
			.status(ArticleCommentStatus.ACTIVE)
			.parent(parent)
			.build();

		articleCommentRepository.save(comment);

		return article.toDto(loginMember);
	}

	@Override
	@Transactional
	public void deleteArticleComment(long commentId, OAuth2User principal) {
		ArticleComment comment = articleCommentRepository.findById(commentId).orElseThrow(
			() -> new CustomException.ArticleNotFoundException(ErrorCode.COMMENT_NOT_FOUND.getMessage(),
				ErrorCode.COMMENT_NOT_FOUND)
		);

		Member loginMember = getLoginMemberByPrincipal(principal);

		if (loginMember == null) {
			throw new CustomException.MemberNotFound(ErrorCode.MEMBER_NOTFOUND.getMessage(), ErrorCode.MEMBER_NOTFOUND);
		}

		if (!comment.isWrittenBy(loginMember) && !loginMember.isAdmin()) {
			throw new CustomException.MemberNotPermittedException(ErrorCode.MEMBER_NOT_PERMITTED.getMessage(),
				ErrorCode.MEMBER_NOT_PERMITTED);
		}

		comment.delete();
	}

	@Override
	@Transactional
	public ArticleResponseDto.ArticleResponse likeArticle(long articleId, OAuth2User principal) {
		Article article = articleRepository.findById(articleId).orElseThrow(
			() -> new CustomException.ArticleNotFoundException(ErrorCode.ARTICLE_NOT_FOUND.getMessage(),
				ErrorCode.ARTICLE_NOT_FOUND)
		);

		Member loginMember = getLoginMemberByPrincipal(principal);

		if (loginMember == null) {
			throw new CustomException.MemberNotPermittedException(ErrorCode.MEMBER_NOT_PERMITTED.getMessage(),
				ErrorCode.MEMBER_NOT_PERMITTED);
		}

		Optional<ArticleHeart> existedHeart = articleHeartRepository.findByMemberAndArticle(loginMember, article);

		if (existedHeart.isEmpty()) {
			ArticleHeart heart = articleHeartRepository.save(ArticleHeart.builder()
				.article(article)
				.member(loginMember)
				.build());

			return heart.toDto(loginMember);
		}

		articleHeartRepository.delete(existedHeart.get());
		return ArticleResponseDto.ArticleResponse.builder()
			.id(article.getId())
			.heartNum(article.getHeartNum())
			.isHeart(false)
			.build();
	}

	@Override
	@Transactional
	public ArticleResponseDto.ArticleResponse bookmarkArticle(long articleId, OAuth2User principal) {
		Article article = articleRepository.findById(articleId).orElseThrow(
			() -> new CustomException.ArticleNotFoundException(ErrorCode.ARTICLE_NOT_FOUND.getMessage(),
				ErrorCode.ARTICLE_NOT_FOUND)
		);

		Member loginMember = getLoginMemberByPrincipal(principal);

		if (loginMember == null) {
			throw new CustomException.MemberNotPermittedException(ErrorCode.MEMBER_NOT_PERMITTED.getMessage(),
				ErrorCode.MEMBER_NOT_PERMITTED);
		}

		Optional<ArticleBookmark> existedBookmark = articleBookmarkRepository.findByMemberAndArticle(loginMember,
			article);

		if (existedBookmark.isEmpty()) {
			ArticleBookmark bookmark = articleBookmarkRepository.save(ArticleBookmark.builder()
				.article(article)
				.member(loginMember)
				.build());

			return bookmark.toDto(loginMember);
		}

		articleBookmarkRepository.delete(existedBookmark.get());
		return ArticleResponseDto.ArticleResponse.builder()
			.id(article.getId())
			.bookmarkNum(article.getBookmarkNum())
			.isBookmark(false)
			.build();
	}

	private Member getLoginMemberByPrincipal(OAuth2User principal) {
		String email = principal.getName();

		return memberService.getLoginMemberByEmail(email);
	}
}
