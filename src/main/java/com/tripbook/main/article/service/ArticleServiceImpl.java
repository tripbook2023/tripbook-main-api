package com.tripbook.main.article.service;

import java.util.Arrays;
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
import com.tripbook.main.article.entity.ArticleReport;
import com.tripbook.main.article.enums.ArticleCommentStatus;
import com.tripbook.main.article.enums.ArticleStatus;
import com.tripbook.main.article.repository.ArticleBookmarkRepository;
import com.tripbook.main.article.repository.ArticleCommentRepository;
import com.tripbook.main.article.repository.ArticleHeartRepository;
import com.tripbook.main.article.repository.ArticleImageRepository;
import com.tripbook.main.article.repository.ArticleReportRepository;
import com.tripbook.main.article.repository.ArticleRepository;
import com.tripbook.main.global.entity.Image;
import com.tripbook.main.global.entity.Location;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;
import com.tripbook.main.global.repository.ImageRepository;
import com.tripbook.main.global.repository.LocationRepository;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
	private final MemberService memberService;
	private final ArticleRepository articleRepository;
	private final ImageRepository imageRepository;
	private final ArticleImageRepository articleImageRepository;
	private final ArticleCommentRepository articleCommentRepository;
	private final ArticleBookmarkRepository articleBookmarkRepository;
	private final ArticleHeartRepository articleHeartRepository;
	private final ArticleReportRepository articleReportRepository;
	private final LocationRepository locationRepository;

	@Override
	@Transactional
	public ArticleResponseDto.ArticleResponse saveArticle(ArticleRequestDto.ArticleSaveRequest requestDto,
		ArticleStatus status, OAuth2User principal) {
		Member loginMember = getLoginMemberByPrincipal(principal);
		Article article;
		if (loginMember == null) {
			throw new CustomException.MemberNotFound(ErrorCode.MEMBER_NOTFOUND.getMessage(), ErrorCode.MEMBER_NOTFOUND);
		}
		if (requestDto.getArticleId() != null) {
			Optional<Article> resultDto = articleRepository.findById(requestDto.getArticleId());
			resultDto.ifPresentOrElse(targetArticle -> {
				//업데이트
				targetArticle.updateArticle(requestDto, status);
				imageRefIdMapping(requestDto.getFileIds(), targetArticle.getId());
				//태그 저장
				// initTagList(requestDto, targetArticle);
				//위치 장소 저장
				initLocation(requestDto, targetArticle);
			}, () -> {
				//Empty
				throw new CustomException.ArticleNotFoundException(ErrorCode.ARTICLE_NOT_FOUND.getMessage(),
					ErrorCode.ARTICLE_NOT_FOUND);
			});
			// 성공시 return
			return ArticleResponseDto.ArticleResponse.builder()
				.id(requestDto.getArticleId())
				.title(requestDto.getTitle())
				.build();
		} else {
			// 저장
			article = articleRepository.save(Article.builder()
				.title(requestDto.getTitle())
				.content(requestDto.getContent())
				.member(loginMember)
				.thumbnailUrl(requestDto.getThumbnail())
				.status(status)
				.build());
			imageRefIdMapping(requestDto.getFileIds(), article.getId());
			//태그 저장
			// initTagList(requestDto, article);
			//위치 장소 저장
			initLocation(requestDto, article);

			return ArticleResponseDto.ArticleResponse.builder()
				.id(article.getId())
				.title(article.getTitle())
				.build();

		}
	}

	private void initLocation(ArticleRequestDto.ArticleSaveRequest requestDto, Article article) {
		if (requestDto.getLocationList() == null || requestDto.getLocationList().isEmpty())
			return;
		//기존 여행장소 리스트 제거
		deleteLocationList(article);
		if (requestDto.getLocationList() != null) {
			List<Location> locationList = requestDto.getLocationList().stream().map(locationItem -> Location.builder()
				.x(locationItem.getLocationX())
				.y(locationItem.getLocationY())
				.name(locationItem.getName())
				.article(article)
				.build()).toList();
			locationRepository.saveAll(locationList);
		}
		// locationRepository.saveAll(requestDto.getLocationList());
		// requestDto.getLocationList().forEach(locationItem -> {
		// 	if (locationItem.getLocationX() != null && locationItem.getLocationY() != null
		// 		&& locationItem.getName() != null) {
		// 		locationRepository.save(Location.builder()
		// 			.x(locationItem.getLocationX())
		// 			.y(locationItem.getLocationY())
		// 			.name(locationItem.getName())
		// 			.article(article)
		// 			.build()
		// 		);
		// 	}
		// });
	}

	private void deleteLocationList(Article article) {
		locationRepository.deleteByArticle(article);
	}

	private void initTagList(ArticleRequestDto.ArticleSaveRequest requestDto, Article article) {
		// if (requestDto.getTagList() != null) {
		// 	articleTagRepository.deleteAllByArticle(article);
		// 	requestDto.getTagList().forEach(tag -> {
		// 		articleTagRepository.save(ArticleTag.builder().name(tag).article(article).build());
		// 	});
		// }
	}

	private void deleteArticleImagesAndArticleTags(ArticleRequestDto.ArticleSaveRequest requestDto) {
		//기존 이미지, 태그 삭제
		articleImageRepository.deleteAllByArticleId(requestDto.getArticleId());
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
		//
		return articleRepository
			.getAllByTitleContainingOrContentContainingAndStatusAndLocationName(word, word, ArticleStatus.ACTIVE,
				pageable)
			.map(article -> article.toDto(loginMember));

	}

	@Override
	@Transactional(readOnly = true)
	public ArticleResponseDto.ArticleResponse getArticleDetail(long articleId, OAuth2User principal) {
		Article article = getArticle(articleId);

		Member loginMember = getLoginMemberByPrincipal(principal);

		if (article.getStatus().equals(ArticleStatus.DELETED)) {
			throw new CustomException.ArticleDeletedException(ErrorCode.ARTICLE_DELETED.getMessage(),
				ErrorCode.ARTICLE_DELETED);
		}

		return article.toDto(loginMember);
	}

	private Article getArticle(long articleId) {
		Article article = articleRepository.findById(articleId).orElseThrow(
			() -> new CustomException.ArticleNotFoundException(ErrorCode.ARTICLE_NOT_FOUND.getMessage(),
				ErrorCode.ARTICLE_NOT_FOUND)
		);
		return article;
	}

	@Override
	@Transactional
	public void deleteArticle(long articleId, OAuth2User principal) {
		Article article = getArticle(articleId);

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
		Article article = getArticle(articleId);

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
		Article article = getArticle(articleId);

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

			return heart.toDto(loginMember, articleHeartRepository.countByArticle(article));
		}

		articleHeartRepository.delete(existedHeart.get());
		return ArticleResponseDto.ArticleResponse.builder()
			.id(article.getId())
			.heartNum(articleHeartRepository.countByArticle(article))
			.isHeart(false)
			.build();
	}

	@Override
	@Transactional
	public ArticleResponseDto.ArticleResponse bookmarkArticle(long articleId, OAuth2User principal) {
		Article article = getArticle(articleId);

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

	@Override
	@Transactional
	public ArticleResponseDto.ArticleResponse reportArticle(ArticleRequestDto.ReportRequest requestDto,
		OAuth2User principal) {
		Member loginMember = getLoginMemberByPrincipal(principal);
		Article article = getArticle(requestDto.getArticleId());

		// 해당 Member가 Article Report 이력이 있다면, count
		Optional<ArticleReport> articleReport = articleReportRepository.findByAndArticleAndMember(
			article, loginMember);
		articleReport.ifPresentOrElse(targetArticleReport -> {
			//이미 신고한 이력이 있는 회원
			throw new CustomException.ArticleInternalException(ErrorCode.ARTICLE_INTERNAL.getMessage(),
				ErrorCode.ARTICLE_INTERNAL);
		}, () -> {
			// articleReport Save
			articleReportRepository.save(ArticleReport.builder()
				.article(article)
				.member(loginMember)
				.content(requestDto.getContent())
				.build());

		});
		return ArticleResponseDto.ArticleResponse.builder()
			.id(article.getId())
			.title(article.getTitle())
			.build();

	}

	private Member getLoginMemberByPrincipal(OAuth2User principal) {
		String email = principal.getName();

		return memberService.getLoginMemberByEmail(email);
	}

	private void imageRefIdMapping(long[] imageArr, long refId) {
		if (imageArr == null) {
			return;
		}

		// 기존 이미지 연결 끊기 isEnable=false
		imageRepository.updateByRefIdReset(refId);
		// 새로운 이미지 refId Update
		Arrays.stream(imageArr).forEach(targetId -> {
			Optional<Image> image = imageRepository.findById(targetId);
			image.ifPresent(targetImage -> targetImage.updateRefId(refId));
		});
	}
}
