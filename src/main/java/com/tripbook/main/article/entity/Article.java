package com.tripbook.main.article.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import com.tripbook.main.article.dto.ArticleRequestDto;
import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.enums.ArticleStatus;
import com.tripbook.main.global.common.BasicEntity;
import com.tripbook.main.global.dto.LocationDto;
import com.tripbook.main.global.entity.Location;
import com.tripbook.main.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Lob()
	private String content;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ArticleStatus status;

	@OneToMany(mappedBy = "article")
	private List<ArticleTag> tagList = new ArrayList<>();

	@OneToMany(mappedBy = "article")
	private List<ArticleHeart> heartList = new ArrayList<>();

	@OneToMany(mappedBy = "article")
	private List<ArticleBookmark> bookmarkList = new ArrayList<>();

	@Where(clause = "status != 'DELETED'")
	@OneToMany(mappedBy = "article")
	private List<ArticleComment> commentList = new ArrayList<>();
	@OneToOne(mappedBy = "article")
	private Location location;
	@Column
	private String thumbnailUrl;
	@Formula("(select count(*) from TB_ARTICLE_HEART h where h.article_id = id)")
	private long heartNum;

	@Formula("(select count(*) from TB_ARTICLE_BOOKMARK b where b.article_id = id)")
	private long bookmarkNum;

	@Formula("(select count(*) from TB_ARTICLE_COMMENT c where c.article_id = id)")
	private long commentNum;

	@Builder
	public Article(String title, String content, ArticleStatus status, Member member,
		List<ArticleHeart> heartList, List<ArticleBookmark> bookmarkList,
		List<ArticleComment> commentList, String thumbnailUrl) {
		this.title = title;
		this.content = content;
		this.status = status;
		this.member = member;
		this.heartList = heartList;
		this.bookmarkList = bookmarkList;
		this.commentList = commentList;
		this.thumbnailUrl = thumbnailUrl;
	}

	public void updateArticle(ArticleRequestDto.ArticleSaveRequest articleSaveRequest, ArticleStatus status) {
		this.title = articleSaveRequest.getTitle();
		this.content = articleSaveRequest.getContent();
		this.thumbnailUrl = articleSaveRequest.getThumbnail();
		this.status = status;
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

	private void updateTempArticle(ArticleRequestDto.ArticleSaveRequest request) {
		this.title = request.getTitle();
		this.content = request.getContent();
	}

	public ArticleResponseDto.ArticleResponse toDto(Member member) {

		return ArticleResponseDto.ArticleResponse.builder()
			.id(this.id)
			.title(this.title)
			.content(this.content)
			.author(this.member.toSimpleDto())
			.heartNum(this.heartNum)
			.isHeart(this.heartList != null
				&& this.heartList.stream().filter(h -> h.getMember() == member).toList().size() > 0)
			.bookmarkNum(this.bookmarkNum)
			.isBookmark(this.bookmarkList != null
				&& this.bookmarkList.stream().filter(h -> h.getMember() == member).toList().size() > 0)
			.commentList(
				this.commentList == null ? new ArrayList<>() :
					this.commentList.stream().map(ArticleComment::toDto).toList())
			.location(this.location == null ? null : new LocationDto.LocationSimpleDto(this.location))
			.createdAt(this.getCreatedAt())
			.updatedAt(this.getUpdatedAt())
			.thumbnailUrl(this.thumbnailUrl)
			.tagList(this.tagList == null ? new ArrayList<>() : this.tagList.stream().map(ArticleTag::getName).toList())
			.build();

	}
}
