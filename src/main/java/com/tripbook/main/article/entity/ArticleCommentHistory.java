package com.tripbook.main.article.entity;

import com.tripbook.main.article.enums.ArticleCommentStatus;
import com.tripbook.main.global.common.BasicEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TB_ARTICLE_COMMENT_HISTORY")
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleCommentHistory extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private ArticleComment comment;

    @Column
    @Enumerated(EnumType.STRING)
    private ArticleCommentStatus statusFrom;

    @Column
    @Enumerated(EnumType.STRING)
    private ArticleCommentStatus statusTo;

    @Builder
    public ArticleCommentHistory(ArticleComment comment, ArticleCommentStatus from, ArticleCommentStatus to) {
        this.comment = comment;
        this.statusFrom = from;
        this.statusTo = to;
    }

}
