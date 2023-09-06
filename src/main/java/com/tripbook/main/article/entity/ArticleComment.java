package com.tripbook.main.article.entity;

import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.enums.ArticleCommentStatus;
import com.tripbook.main.global.common.BasicEntity;
import com.tripbook.main.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_ARTICLE_COMMENT")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleComment extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ArticleCommentStatus status;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ArticleComment parent;

    @OneToMany(mappedBy = "parent")
    private List<ArticleComment> childList = new ArrayList<>();

    @Builder
    public ArticleComment(Member member, Article article, String content, ArticleComment parent, ArticleCommentStatus status) {
        this.member = member;
        this.article = article;
        this.content = content;
        this.parent = parent;
        this.status = status;
    }

    public boolean isWrittenBy(Member member) {
        if (member == null) {
            return false;
        }

        return this.member == member;
    }

    public void delete() {
        this.status = ArticleCommentStatus.DELETED;
    }

    public ArticleResponseDto.CommentResponse toDto() {
        return ArticleResponseDto.CommentResponse.builder()
                .id(this.id)
                .content(this.content)
                .author(this.member.toSimpleDto())
                .childList(this.childList.stream().map(ArticleComment::toDto).toList())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}