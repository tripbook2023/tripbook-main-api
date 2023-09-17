package com.tripbook.main.article.entity;

import com.tripbook.main.article.dto.ArticleResponseDto;
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
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Length;

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

    @Column(nullable = false)
    @Length(min = 800, max = 10000)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @OneToMany
    private List<ArticleTag> tagList = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<ArticleHeart> heartList = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<ArticleBookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<ArticleComment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<ArticleImage> imageList = new ArrayList<>();

    @Formula("(select count(*) from TB_ARTICLE_HEART h where h.article_id = id)")
    private long heartNum;

    @Formula("(select count(*) from TB_ARTICLE_BOOKMARK b where b.article_id = id)")
    private long bookmarkNum;

    @Formula("(select count(*) from TB_ARTICLE_COMMENT c where c.article_id = id)")
    private long commentNum;

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

    public boolean isApproved() {
        return this.status.equals(ArticleStatus.APPROVED);
    }

    public boolean isWrittenBy(Member member) {
        if (member == null) {
            return false;
        }

        return this.member == member;
    }

    public void delete() {
        this.status = ArticleStatus.DELETED;
    }

    public ArticleResponseDto.ArticleResponse toDto(Member member) {

        return ArticleResponseDto.ArticleResponse.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .author(this.member.toSimpleDto())
                .heartNum(this.heartNum)
                .isHeart(this.heartList.stream().filter(h -> h.getMember() == member).toList().size() > 0)
                .bookmarkNum(this.bookmarkNum)
                .isBookmark(this.bookmarkList.stream().filter(h -> h.getMember() == member).toList().size() > 0)
                .commentList(this.commentList.stream().map(ArticleComment::toDto).toList())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .imageList(this.imageList.stream().map(ArticleImage::toDto).toList())
                .tagList(this.tagList.stream().map(ArticleTag::getName).toList())
                .build();

    }
}
