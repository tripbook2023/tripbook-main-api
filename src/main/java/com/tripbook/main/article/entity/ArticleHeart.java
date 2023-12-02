package com.tripbook.main.article.entity;

import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.global.common.BasicEntity;
import com.tripbook.main.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TB_ARTICLE_HEART")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleHeart extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder
    public ArticleHeart(Member member, Article article) {
        this.member = member;
        this.article = article;
    }

    public ArticleResponseDto.ArticleResponse toDto(Member member,Long heartNum) {
        return ArticleResponseDto.ArticleResponse.builder()
                .id(this.article.getId())
                .isHeart(this.member.equals(member))
                .heartNum(heartNum)
                .build();
    }
}
