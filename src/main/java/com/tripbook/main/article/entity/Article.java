package com.tripbook.main.article.entity;

import com.tripbook.main.article.enums.ArticleStatus;
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
@Table(name = "TB_ARTICLE")
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    private List<ArticleHeart> heartList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    private List<ArticleBookmark> bookmarkList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    private List<ArticleComment> commentList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    private List<ArticleImage> imageList = new ArrayList<>();

    @Builder
    public Article(String title, String content, ArticleStatus status, Member member,
                   List<ArticleHeart> heartList, List<ArticleBookmark> bookmarkList,
                   List<ArticleComment> commentList, List<ArticleImage> imageList) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.member = member;
        this.heartList = heartList;
        this.bookmarkList = bookmarkList;
        this.commentList = commentList;
        this.imageList = imageList;
    }
}
