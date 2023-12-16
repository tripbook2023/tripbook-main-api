package com.tripbook.main.global.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.article.entity.Article;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_LOCATION")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String x;
	private String y;
	private String name;
	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;

	@Builder
	public Location(String x, String y, String name, Article article) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.article = article;
	}

	public ArticleResponseDto.LocationResponse toDto() {
		return ArticleResponseDto.LocationResponse.builder()
			.id(this.id)
			.locationX(this.x)
			.locationY(this.y)
			.name(this.name)
			.build();
	}
}
