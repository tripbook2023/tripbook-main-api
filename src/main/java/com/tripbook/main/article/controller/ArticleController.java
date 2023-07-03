package com.tripbook.main.article.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    // 북마크 목록, 임시저장 목록

    @PostMapping()
    public ResponseEntity<?> saveArticle() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping()
    public ResponseEntity<?> getArticles() {
        // Pageable
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<?> getArticle() {
        //
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticle() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/{articleId}/comment")
    public ResponseEntity<?> saveComment() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/{articleId}/comment")
    public ResponseEntity<?> getComments() {
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/{articleId}/comment/{commentId}")
    public ResponseEntity<?> deleteComment() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/{articleId}/like")
    public ResponseEntity<?> likeArticle() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/{articleId}/archive")
    public ResponseEntity<?> bookmarkArticle() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/temp")
    public ResponseEntity<?> saveTempArticle() {
        return ResponseEntity.ok("ok");
    }

}
