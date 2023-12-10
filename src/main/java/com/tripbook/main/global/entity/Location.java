package com.tripbook.main.global.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.tripbook.main.article.entity.Article;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
	@OneToOne
	@JoinColumn(name = "article_id")
	private Article article;

	@Builder
	public Location(String x, String y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
	}
}
