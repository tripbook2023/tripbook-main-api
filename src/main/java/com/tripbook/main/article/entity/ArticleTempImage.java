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
@Table(name = "TB_ARTICLETEMP_IMAGE")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleTempImage extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "articleTemp_id")
    private ArticleTemp articleTemp;

    @Builder
    public ArticleTempImage(Image image, ArticleTemp articleTemp) {
        this.image = image;
        this.articleTemp = articleTemp;
    }
}
