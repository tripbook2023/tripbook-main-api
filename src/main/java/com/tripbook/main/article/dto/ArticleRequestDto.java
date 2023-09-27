package com.tripbook.main.article.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ArticleRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ArticleSaveRequest {
        @Schema(description = "제목")
        @NotNull(message = "Title is required")
        private String title;

        @Schema(description = "내용")
        @NotNull(message = "Content is required")
        private String content;

        @Schema(description = "썸네일")
        private MultipartFile thumbnail;

        @Schema(description = "이미지 리스트")
        private List<MultipartFile> imageList;

        @Schema(description = "테그 리스트")
        private List<String> tagList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ArticleEditRequest {
        @Schema(description = "제목")
        @NotNull(message = "Title is required")
        private String title;

        @Schema(description = "내용")
        @NotNull(message = "Content is required")
        private String content;

        @Schema(description = "썸네일")
        private MultipartFile thumbnail;

        @Schema(description = "이미지 리스트")
        private List<MultipartFile> imageList;

        @Schema(description = "테그 리스트")
        private List<String> tagList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CommentSaveRequest {

        @Schema(description = "댓글 내용")
        @NotNull(message = "Content is required")
        private String content;

        @Schema(description = "상위 댓글 ID")
        @NotNull(message = "parentId is required")
        private long parentId;
    }

}
