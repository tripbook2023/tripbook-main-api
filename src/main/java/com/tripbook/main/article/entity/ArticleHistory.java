package com.tripbook.main.article.entity;

import com.tripbook.main.article.enums.ArticleStatus;
import com.tripbook.main.global.common.BasicEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TB_ARTICLE_HISTORY")
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleHistory extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Column
    @Enumerated(EnumType.STRING)
    private ArticleStatus statusFrom;

    @Column
    @Enumerated(EnumType.STRING)
    private ArticleStatus statusTo;

    @Builder
    public ArticleHistory(Article article, ArticleStatus from, ArticleStatus to) {
        this.article = article;
        this.statusFrom = from;
        this.statusTo = to;
    }

}
