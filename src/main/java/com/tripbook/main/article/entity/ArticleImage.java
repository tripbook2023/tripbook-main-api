package com.tripbook.main.article.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.tripbook.main.article.dto.ArticleResponseDto;
import com.tripbook.main.global.common.BasicEntity;
import com.tripbook.main.global.entity.Image;

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
@Table(name = "TB_ARTICLE_IMAGE")
@Getter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleImage extends BasicEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Boolean isThumbnail;
	@ManyToOne
	@JoinColumn(name = "image_id")
	private Image image;

	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;

	@Builder
	public ArticleImage(Image image, Article article, Boolean isThumbnail) {
		this.image = image;
		this.article = article;
		this.isThumbnail = isThumbnail;
	}
	public void updateEnable(boolean enable){
		this.setIsEnable(enable);
	}
	public ArticleResponseDto.ImageResponse toDto() {
		return ArticleResponseDto.ImageResponse.builder()
			.id(this.id)
			.url(this.image.getUrl())
			.build();
	}
}
