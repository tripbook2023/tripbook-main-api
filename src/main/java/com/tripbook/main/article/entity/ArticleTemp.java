package com.tripbook.main.article.entity;

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
@Table(name = "TB_ARTICLE_TEMP")
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleTemp extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "articleTemp")
    private List<ArticleTempImage> imageList = new ArrayList<>();

    @Builder
    public ArticleTemp(String title, String content, Member member, List<ArticleTempImage> imageList) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.imageList = imageList;
    }
}
