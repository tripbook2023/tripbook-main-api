package com.tripbook.main.article.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tripbook.main.article.dto.ArticleRequestDto;
import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.enums.ArticleSort;
import com.tripbook.main.article.enums.ArticleStatus;
import com.tripbook.main.article.service.ArticleService;
import com.tripbook.main.global.common.ErrorResponse;
import com.tripbook.main.global.enums.ErrorCode;
import com.tripbook.main.global.exception.CustomException;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/articles")
@Tag(name = "Articles", description = "Article API")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;

	@Operation(security = {
		@SecurityRequirement(name = "JWT")},
		summary = "여행소식 저장", description = "여행소식을 저장합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ArticleResponseDto.ArticleResponse.class))),
		@ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> saveArticle(@Valid @ModelAttribute ArticleRequestDto.ArticleSaveRequest requestDto,
		Authentication authentication) {
		if (!requestDto.getImageAccept()) {
			List<String> imageList = requestDto.getImageList()
				.stream()
				.map(MultipartFile::getOriginalFilename)
				.toList();
			log.error("Image Not Accepted::{},{}", imageList, requestDto.getThumbnail().getOriginalFilename());
			throw new CustomException.UnsupportedImageFileException(
				imageList.toString().concat("||").concat(requestDto.getThumbnail().getOriginalFilename()),
				ErrorCode.FILE_UNSUPPORTED_ERROR);
		}
		OAuth2User principal = (OAuth2User)authentication.getPrincipal();
		return ResponseEntity.ok(articleService.saveArticle(requestDto, ArticleStatus.ACTIVE, principal));
	}

	@Operation(summary = "여행소식 목록 조회 및 검색",
		security = {
			@SecurityRequirement(name = "JWT")},
		responses = {
			@ApiResponse(responseCode = "200", description = "성공 \n\n 'content'배열 내의 값은 여행소식 저장API 성공시 반환하는 값을 참고해주세요.",
				content = @Content(schema = @Schema(implementation = Slice.class))),
		})
	@Parameters(value = {
		@Parameter(name = "word", description = "검색어", in = ParameterIn.QUERY, required = false),
		@Parameter(name = "page", description = "페이지 번호 (Default : 0)", in = ParameterIn.QUERY),
		@Parameter(name = "size", description = "페이지당 게시글 수 (Default : 10)", in = ParameterIn.QUERY),
		@Parameter(name = "sort", description = "정렬 기준 (Default : createdDesc)\n CREATED_ASC, CREATED_DESC, POPULARITY 중 1",
			in = ParameterIn.QUERY)
	})
	@GetMapping()
	public ResponseEntity<?> searchArticle(@Nullable @RequestParam String word, Authentication authentication,
		@Nullable @RequestParam Integer page,
		@Nullable @RequestParam Integer size,
		@Nullable @RequestParam String sort) {
		OAuth2User principal = (OAuth2User)authentication.getPrincipal();

		Sort pageSort = getPageSort(
			sort == null || sort.isBlank() ? ArticleSort.CREATED_DESC : ArticleSort.valueOf(sort));
		if (Objects.isNull(page)) {
			page = 0;
		}
		if (Objects.isNull(size)) {
			size = 10;
		}

		Pageable pageable = PageRequest.of(page, size, pageSort);
		return ResponseEntity.ok(articleService.searchArticle(word, pageable, principal));
	}

	@Operation(security = {
		@SecurityRequirement(name = "JWT")},
		summary = "여행소식 상세 조회",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ArticleResponseDto.ArticleResponse.class))),
			@ApiResponse(responseCode = "400", description = "삭제된 여행소식", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "여행소식 찾을 수  없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		})
	@Parameters(value = {
		@Parameter(name = "articleId", description = "여행 소식 ID", in = ParameterIn.PATH),
	})
	@GetMapping("/{articleId}")
	public ResponseEntity<?> getArticle(@PathVariable long articleId, Authentication authentication) {
		OAuth2User principal = (OAuth2User)authentication.getPrincipal();
		return ResponseEntity.ok(articleService.getArticleDetail(articleId, principal));
	}

	@Operation(summary = "여행소식 삭제", security = {@SecurityRequirement(name = "JWT")},
		responses = {
			@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
			@ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		})
	@Parameters(value = {
		@Parameter(name = "articleId", description = "여행 소식 ID", in = ParameterIn.PATH),
	})
	@DeleteMapping("/{articleId}")
	public void deleteArticle(@PathVariable long articleId, Authentication authentication) {
		OAuth2User principal = (OAuth2User)authentication.getPrincipal();

		articleService.deleteArticle(articleId, principal);
	}

	@Operation(summary = "댓글 작성", security = {@SecurityRequirement(name = "JWT")},
		responses = {
			@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ArticleResponseDto.CommentResponse.class))),
			@ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		})
	@Parameters(value = {
		@Parameter(name = "articleId", description = "여행 소식 ID", in = ParameterIn.PATH),
	})
	@PostMapping(value = "/{articleId}/comments")
	public ResponseEntity<?> saveComment(@PathVariable long articleId,
		@Valid @RequestBody ArticleRequestDto.CommentSaveRequest requestDto, Authentication authentication) {
		OAuth2User principal = (OAuth2User)authentication.getPrincipal();

		return ResponseEntity.ok(articleService.saveArticleComment(articleId, requestDto, principal));
	}

	@Operation(summary = "댓글 삭제", security = {@SecurityRequirement(name = "JWT")},
		responses = {
			@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
			@ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		})
	@Parameters(value = {
		@Parameter(name = "commentId", description = "댓글 ID", in = ParameterIn.PATH),
	})
	@DeleteMapping("/comments/{commentId}")
	public void deleteComment(@PathVariable long commentId, Authentication authentication) {
		OAuth2User principal = (OAuth2User)authentication.getPrincipal();

		articleService.deleteArticleComment(commentId, principal);
	}

	@Operation(summary = "여행소식 좋아요", security = {@SecurityRequirement(name = "JWT")},
		responses = {
			@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
			@ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		})
	@Parameters(value = {
		@Parameter(name = "articleId", description = "여행 소식 ID", in = ParameterIn.PATH),
	})
	@PostMapping("/{articleId}/like")
	public ResponseEntity<?> likeArticle(@PathVariable long articleId, Authentication authentication) {

		OAuth2User principal = (OAuth2User)authentication.getPrincipal();

		return ResponseEntity.ok(articleService.likeArticle(articleId, principal));
	}

	@Operation(summary = "여행소식 북마크", security = {@SecurityRequirement(name = "JWT")},
		responses = {
			@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
			@ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		})
	@Parameters(value = {
		@Parameter(name = "articleId", description = "여행 소식 ID", in = ParameterIn.PATH),
	})
	@PostMapping("/{articleId}/bookmark")
	public ResponseEntity<?> bookmarkArticle(@PathVariable long articleId, Authentication authentication) {

		OAuth2User principal = (OAuth2User)authentication.getPrincipal();

		return ResponseEntity.ok(articleService.bookmarkArticle(articleId, principal));
	}

	@Operation(summary = "여행소식 임시저장", security = {@SecurityRequirement(name = "JWT")},
		responses = {
			@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ArticleResponseDto.ArticleResponse.class))),
			@ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		})
	@PostMapping(value = "/temp", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> saveTempArticle(@Valid @ModelAttribute ArticleRequestDto.ArticleSaveRequest requestDto,
		Authentication authentication) {

		OAuth2User principal = (OAuth2User)authentication.getPrincipal();

		return ResponseEntity.ok(articleService.saveArticle(requestDto, ArticleStatus.TEMP, principal));
	}

	private Sort getPageSort(ArticleSort sortParam) {
		return switch (sortParam) {
			case CREATED_ASC -> Sort.by("createdAt").ascending();
			case CREATED_DESC -> Sort.by("createdAt").descending();
			case POPULARITY -> Sort.by("heartNum").descending()
				.and(Sort.by("commentNum").descending())
				.and(Sort.by("bookmarkNum").descending());
		};
	}
}
