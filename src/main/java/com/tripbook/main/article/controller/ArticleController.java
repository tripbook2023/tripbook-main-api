package com.tripbook.main.article.controller;

import com.tripbook.main.article.dto.ArticleRequestDto;
import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.global.common.ErrorResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@Tag(name = "Articles", description = "Article API")
@Slf4j
public class ArticleController {


    @Operation(security = {
            @SecurityRequirement(name = "JWT")},
            summary = "여행소식 저장", description = "여행소식을 저장합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ArticleResponseDto.ArticleResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> saveArticle(@Valid @RequestBody ArticleRequestDto.ArticleSaveRequest requestDto) {
        return ResponseEntity.ok("ok");
    }

    @Operation(summary = "여행소식 목록 조회 및 검색",
            responses = {
            @ApiResponse(responseCode = "200", description = "성공 \n\n 'content'배열 내의 값은 여행소식 저장API 성공시 반환하는 값을 참고해주세요.",
                         content = @Content(schema = @Schema(implementation = Slice.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Parameters(value = {
            @Parameter(name = "word", description = "검색어", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호 (Default : 0)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지당 게시글 수 (Default : 10)", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬 기준 (Default : [createdAt-DESC])",
                        example = "[createdAt-DESC, popularity-ASC]", in = ParameterIn.QUERY)
    })
    @GetMapping()
    public ResponseEntity<?> getArticles(
            @RequestParam String word,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort) {

        return ResponseEntity.ok("ok");
    }

    @Operation(summary = "여행소식 상세 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ArticleResponseDto.ArticleResponse.class))),
                    @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    @Parameters(value = {
            @Parameter(name = "articleId", description = "여행 소식 ID", in = ParameterIn.PATH),
    })
    @GetMapping("/{articleId}")
    public ResponseEntity<?> getArticle(@PathVariable long articleId) {
        // , page, size, sort
        return ResponseEntity.ok("ok");
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
    public ResponseEntity<?> deleteArticle(@PathVariable long articleId) {
        return ResponseEntity.ok("ok");
    }

    @Operation(summary = "댓글 작성", security = {@SecurityRequirement(name = "JWT")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ArticleResponseDto.CommentResponse.class))),
                    @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    @Parameters(value = {
            @Parameter(name = "articleId", description = "여행 소식 ID", in = ParameterIn.PATH),
    })
    @PostMapping(value = "/{articleId}/comment")
    public ResponseEntity<?> saveComment(@PathVariable long articleId, @Valid @RequestBody ArticleRequestDto.CommentSaveRequest requestDto) {
        return ResponseEntity.ok("ok");
    }


    @Operation(summary = "댓글 삭제", security = {@SecurityRequirement(name = "JWT")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    @Parameters(value = {
            @Parameter(name = "commentId", description = "댓글 ID", in = ParameterIn.PATH),
    })
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable long commentId) {
        return ResponseEntity.ok("ok");
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
    public ResponseEntity<?> likeArticle(@PathVariable long articleId) {
        return ResponseEntity.ok("ok");
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
    public ResponseEntity<?> bookmarkArticle(@PathVariable long articleId) {
        return ResponseEntity.ok("ok");
    }

    @Operation(summary = "여행소식 임시저장", security = {@SecurityRequirement(name = "JWT")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ArticleResponseDto.ArticleResponse.class))),
                    @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    @PostMapping("/temp")
    public ResponseEntity<?> saveTempArticle(@Valid @RequestBody ArticleRequestDto.ArticleSaveRequest requestDto) {
        return ResponseEntity.ok("ok");
    }

}
