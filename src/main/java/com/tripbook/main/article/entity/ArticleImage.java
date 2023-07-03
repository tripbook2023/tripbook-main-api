package com.tripbook.main.article.entity;

import com.tripbook.main.global.common.BasicEntity;
import com.tripbook.main.global.entity.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TB_ARTICLE_IMAGE")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleImage extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder
    public ArticleImage(Image image, Article article) {
        this.image = image;
        this.article = article;
    }
}
