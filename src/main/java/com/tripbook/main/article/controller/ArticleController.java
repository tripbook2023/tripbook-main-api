package com.tripbook.main.article.controller;

import com.tripbook.main.article.dto.ArticleRequestDto;
import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.service.ArticleService;
import com.tripbook.main.article.service.ArticleServiceImpl;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<?> saveArticle(@Valid @RequestBody ArticleRequestDto.ArticleSaveRequest requestDto) {
        OAuth2User principal = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(articleService.saveArticle(requestDto, principal));
    }

    @Operation(summary = "여행소식 목록 조회 및 검색",
            responses = {
            @ApiResponse(responseCode = "200", description = "성공 \n\n 'content'배열 내의 값은 여행소식 저장API 성공시 반환하는 값을 참고해주세요.",
                         content = @Content(schema = @Schema(implementation = Slice.class))),
    })
    @Parameters(value = {
            @Parameter(name = "word", description = "검색어", in = ParameterIn.QUERY),
            @Parameter(name = "page", description = "페이지 번호 (Default : 0)", in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "페이지당 게시글 수 (Default : 10)", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬 기준 (Default : createdAt-DESC)",
                        example = "createdAt-DESC, popularity-ASC", in = ParameterIn.QUERY)
    })
    @GetMapping()
    public ResponseEntity<?> searchArticle(@RequestParam String word,
                                            @RequestParam int page,
                                            @RequestParam int size,
                                            @RequestParam List<String> sort) {

        OAuth2User principal = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Sort pageSort = getPageSort(sort);
        if (Objects.isNull(page)){
            page = 0;
        }
        if (Objects.isNull(size)){
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size, pageSort);
        return ResponseEntity.ok(articleService.searchArticle(word, pageable, principal));
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
    public void deleteArticle(@PathVariable long articleId) {
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
    public void deleteComment(@PathVariable long commentId) {
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

    private Sort getPageSort(List<String> sortParam) {
        Sort pageSort = Sort.unsorted();

        if (sortParam.isEmpty()) {
            sortParam.add("createdAt-DESC");
        }

        for (String sort : sortParam) {
            String field = sort.split("-")[0];
            String direction = sort.split("-")[1];

            pageSort = pageSort.and(getSortByOption(field, direction));
        }

        return pageSort;
    }

    private Sort getSortByOption(String field, String direction) {
        if (direction.equals("DESC")) {
            return Sort.by(field).descending();
        }
        return Sort.by(field).ascending();
    }

}
