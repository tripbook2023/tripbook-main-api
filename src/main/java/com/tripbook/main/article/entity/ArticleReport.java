package com.tripbook.main.article.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.tripbook.main.global.common.BasicEntity;
import com.tripbook.main.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_ARTICLE_REPORT")
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleReport extends BasicEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	@Column
	private String content;

	@Builder
	public ArticleReport(Article article, Member member, String content) {
		this.article = article;
		this.member = member;
		this.content = content;
	}
}
